package fileSystem.modal;

/**
 *Class to handle the data block in the pfs file system
 */
public class PfsDataBlockModal {

	byte[] fileData;
	int nextBlockId;
	public PfsDataBlockModal(int fileDataSize){
		fileData = new byte[fileDataSize]	;
		nextBlockId = -1;
	}

	/**
	 * to get the file data
	 * @return
	 */
	public byte[] getFileData() {
		return fileData;
	}

	/**
	 * to set the file data
	 * @param fileData
	 */
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
}
