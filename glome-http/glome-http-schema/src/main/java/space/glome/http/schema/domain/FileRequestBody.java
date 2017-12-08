package space.glome.http.schema.domain;

public class FileRequestBody extends RequestBody {

	private String filePath;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
