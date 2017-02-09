package fileSystem.modal;

import fileSystem.OperationsOfPfsFile;

public class PfsFileManagementBlockModal {

    public static final int fileNameMaxSize = 20;
    public static final int dateMaxSize = 24;
    private String mFilename;
    private String mDateCreated;
    private int mDiskBegID;
    private int mDiskEndID;
    private int mFileSize;
    private boolean isDeleted;



    public PfsFileManagementBlockModal() {
        String emptyStr = OperationsOfPfsFile.getInitData(OperationsOfPfsFile.mDiskBlocks);
        setFilename(emptyStr.substring(0, fileNameMaxSize));
        setCreateDate(emptyStr.substring(0, dateMaxSize));
        setStartingDiskBlockID(-1);
        setEndingDiskBlockID(-1);
        setFileSize(-1);
    }

    public String getFilename() {
        return mFilename;
    }

    public byte[] getFilenameInBytes() {
        return mFilename.getBytes();
    }

    public void setFilename(String mFilename) {
        this.mFilename = mFilename;
    }

    public String getCreateDate() {
        return mDateCreated;
    }

    public byte[] getCreateDateInBytes() {
        return mDateCreated.getBytes();
    }

    public void setCreateDate(String dateCreated) {
        this.mDateCreated = dateCreated;
    }


    public int getStartingDiskBlockID() {
        return mDiskBegID;
    }

    public void setStartingDiskBlockID(int diskBegID) {
        this.mDiskBegID = diskBegID;
    }

    public int getEndingDiskBlockID() {
        return mDiskEndID;
    }

    public void setEndingDiskBlockID(int diskEndID) {
        this.mDiskEndID = diskEndID;
    }

    public int getFileSize() {
        return mFileSize;
    }

    public void setFileSize(int mFileSize) {
        this.mFileSize = mFileSize;
    }
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
