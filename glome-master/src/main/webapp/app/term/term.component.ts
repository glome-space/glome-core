import {Component, OnInit, AfterViewInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';
import {hterm, lib} from 'hterm-umdjs-s6';
import * as io from 'socket.io-client';

import {Account, LoginModalService, Principal} from '../shared';

@Component({
  selector: 'jhi-term',
  templateUrl: './term.component.html',
  styleUrls: [
    'term.css'
  ]

})
export class TermComponent implements OnInit, AfterViewInit {

  constructor(
  ) {
  }

  ngOnInit() {}

  ngAfterViewInit() {
    console.log(hterm);
    console.log(lib);

    const socket = io.connect('http://localhost:8080/', {
      path: `/websocket`
    });

    let term;
    let buf = '';

    function Wetty(argv) {
      this.argv_ = argv;
      this.io = null;
      this.pid_ = -1;
    }

    Wetty.prototype.run = function() {
      this.io = this.argv_.io.push();

      this.io.onVTKeystroke = this.sendString_.bind(this);
      this.io.sendString = this.sendString_.bind(this);
      this.io.onTerminalResize = this.onTerminalResize.bind(this);
    }

    Wetty.prototype.sendString_ = function(str) {
      socket.emit('input', str);
    };

    Wetty.prototype.onTerminalResize = function(col, row) {
      socket.emit('resize', {col, row});
    };

    socket.on('connect', function() {
      lib.init(function() {
        hterm.defaultStorage = new lib.Storage.Local();
        term = new hterm.Terminal();
        (<any>window).term = term;
        term.decorate(document.getElementById('terminal'));
        term.setCursorPosition(0, 0);
        term.setCursorVisible(true);
        term.prefs_.set('ctrl-c-copy', true);
        term.prefs_.set('ctrl-v-paste', true);
        term.prefs_.set('use-default-window-copy', true);

        term.runCommandClass(Wetty, document.location.hash.substr(1));
        socket.emit('resize', {
          col: term.screenSize.width,
          row: term.screenSize.height
        });

        if (buf && buf !== '') {
          term.io.writeUTF16(buf);
          buf = '';
        }
      });
    });

    socket.on('output', function(data) {
      if (!term) {
        buf += data;
        return;
      }
      term.io.writeUTF16(data);
    });

    socket.on('disconnect', function() {
      console.log('Socket.io connection closed');
    });

  }
}
