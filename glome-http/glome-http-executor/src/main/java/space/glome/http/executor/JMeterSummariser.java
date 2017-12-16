package space.glome.http.executor;

import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.RunningSample;

public class JMeterSummariser extends Summariser {

	private static final long serialVersionUID = 0L;

	private final RunningSample total = new RunningSample("TOTAL", 0);

	public JMeterSummariser() {
		super();
	}

	@Override
	public void sampleOccurred(SampleEvent e) {
		SampleResult s = e.getResult();
		synchronized (total) {
			if (s != null) {
				total.addSample(s);
			}
		}
	}

	@Override
	public void sampleStarted(SampleEvent e) {
	}

	@Override
	public void sampleStopped(SampleEvent e) {
	}

	@Override
	public void testStarted() {
	}

	@Override
	public void testEnded() {
	}

	@Override
	public void testStarted(String host) {
	}

	@Override
	public void testEnded(String host) {
	}

	public RunningSample getTotal() {
		return total;
	}
	
	public String printSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of Requests raised=").append(total.getNumSamples()).append('\n');
		sb.append("Total Elapsed Time (ms)=").append(total.getElapsed()).append('\n');
		sb.append("Average Response Time (ms)=").append(total.getAverage()).append('\n');
		sb.append("Max Response Time (ms)=").append(total.getMax()).append('\n');
		sb.append("Min Response Time (ms)=").append(total.getMin()).append('\n');
		sb.append("Response Rate =").append(total.getRateString()).append('\n');
		sb.append("ErrorCount=").append(total.getErrorCount()).append('\n');
		sb.append("ErrorPercentage=").append(total.getErrorPercentage()).append('\n');
		return sb.toString();
	}
}
