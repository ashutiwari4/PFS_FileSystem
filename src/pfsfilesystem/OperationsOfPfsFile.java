package fileSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

import fileSystem.modal.PfsDataBlockModal;
import fileSystem.modal.PfsFileManagementBlockModal;

public class OperationsOfPfsFile {
    // block size
    public static final int mDiskBlocks = 30;
    public static final int mPfsDirInfo = 76;
    public static final int mFileSize = 80;
    public static final int mDirectoryInfo = mPfsDirInfo + mDiskBlocks * mFileSize;
    public static final int mDataBlockSize = 256;
    public static final int linkPtrBlocks = mDiskBlocks + 4;
    public static final int maxAvail_dataBlockSize = 252;
    public static final int bitVector = 0;
    public static final int empty = 0;
    public static final int pfsVolumeSize = 10240;
    public static final String destDir = System.getProperty("user.home") + "/Desktop/ext/";

    PfsFileManagementBlockModal pfsAttributes[];
    private String fileListBitVector;
    private String bitvector;
    private int blockStartingIndex;
    private int pfsFreeDataBlocks;
    private final RandomAccessFile rndmAccess;
    private int noOfFiles;
    private int freeDataBlockIndex;

    byte[] capacity;

    /**
     * parametrized constructor to open the pfs file
     *
     * @param input
     * @throws IOException
     */
    public OperationsOfPfsFile(String input) throws IOException {

        rndmAccess = new RandomAccessFile(input, "rw");

        initAllAttributesPfsDataFileInfo();

        if (getRandomAccessWriter().length() == empty) {

            getRandomAccessWriter().seek(pfsVolumeSize - 1);
            getRandomAccessWriter().write(capacity);
            intializeAllAttributes();
        } else {
            addFileProperties();
            fetchDirMetaInfo();
        }
    }

    /**
     * To intializeAllAttributesialize the pfs file info
     */
    private void initAllAttributesPfsDataFileInfo() {
        capacity = " ".getBytes();
        pfsAttributes = new PfsFileManagementBlockModal[32];
        int i = 0;

        while (i < mDiskBlocks) {
            pfsAttributes[i] = new PfsFileManagementBlockModal();
            i++;
        }
    }

    /**
     * @param str
     * @return
     */

    private int totalZero(String str) {
        int i = 0;
        while (i < str.length()) {
            if (str.charAt(i) != '0')
                return i;
            i++;
        }
        return -1;
    }

    private void addFileProperties() throws IOException {
        byte[] tempBuf;


        for (int fi = 0; fi < mDiskBlocks; fi++) {

            tempBuf = new byte[20];

            getRandomAccessWriter().seek(mPfsDirInfo + fi * mFileSize);
            getRandomAccessWriter().read(tempBuf);
            String lv = new String(tempBuf);

            pfsAttributes[fi].setFilename(lv.substring(0,
                    totalZero(lv) + 1));

            tempBuf = new byte[24];

            getRandomAccessWriter().seek(mPfsDirInfo +
                    fi * mFileSize + PfsFileManagementBlockModal.fileNameMaxSize);

            getRandomAccessWriter().read(tempBuf);
            pfsAttributes[fi].setCreateDate(new String(tempBuf));

            tempBuf = new byte[24];

            getRandomAccessWriter().seek(mPfsDirInfo + fi * mFileSize +
                    PfsFileManagementBlockModal.fileNameMaxSize + PfsFileManagementBlockModal.dateMaxSize);
            getRandomAccessWriter().read(tempBuf);
            lv = new String(tempBuf);


            tempBuf = new byte[4];
            getRandomAccessWriter().seek(mPfsDirInfo + fi * mFileSize +
                    PfsFileManagementBlockModal.fileNameMaxSize + PfsFileManagementBlockModal.dateMaxSize);

            getRandomAccessWriter().read(tempBuf);
            pfsAttributes[fi].setStartingDiskBlockID(
                    FileSystemUtils.convertByteArray(tempBuf));


            tempBuf = new byte[4];
            getRandomAccessWriter().read(tempBuf);
            pfsAttributes[fi].setEndingDiskBlockID(
                    FileSystemUtils.convertByteArray(tempBuf));

            tempBuf = new byte[4];
            getRandomAccessWriter().read(tempBuf);
            pfsAttributes[fi].setFileSize(
                    FileSystemUtils.convertByteArray(tempBuf));
        }

    }

    /**
     * To the
     *
     * @param size
     * @return
     */
    public static String getInitData(int size) {
        String empty = "";
        for (int i = 0; i < size; i++) {
            empty += "0";
        }
        return empty;
    }

    private void pfsWriteAttributes(int fi, PfsFileManagementBlockModal fileInfo) throws IOException {

        getRandomAccessWriter().seek(mPfsDirInfo + fi * mFileSize);
        getRandomAccessWriter().write(fileInfo.getFilenameInBytes());
        getRandomAccessWriter().seek(mPfsDirInfo +
                fi * mFileSize + PfsFileManagementBlockModal.fileNameMaxSize);

        getRandomAccessWriter().write(fileInfo.getCreateDateInBytes());//createDate
        getRandomAccessWriter().seek(mPfsDirInfo + fi * mFileSize +
                PfsFileManagementBlockModal.fileNameMaxSize + PfsFileManagementBlockModal.dateMaxSize);

        getRandomAccessWriter().seek(
                mPfsDirInfo + fi * mFileSize +
                        PfsFileManagementBlockModal.fileNameMaxSize + PfsFileManagementBlockModal.dateMaxSize

        );

        getRandomAccessWriter().write
                (
                        FileSystemUtils.convertIntToArrayBytes(fileInfo.getStartingDiskBlockID())
                );
        getRandomAccessWriter().write(
                FileSystemUtils.convertIntToArrayBytes(fileInfo.getEndingDiskBlockID())
        );
        getRandomAccessWriter().write(
                FileSystemUtils.convertIntToArrayBytes(fileInfo.getFileSize())
        );
    }

    /**
     * @throws IOException
     */
    public void fetchDirMetaInfo() throws IOException {

        byte[] array = new byte[30];
        getRandomAccessWriter().seek(bitVector);
        getRandomAccessWriter().read(array);
        setBitVector(new String(array));

        array = new byte[4];
        getRandomAccessWriter().read(array);
        setFreeStartingIndex(FileSystemUtils.convertByteArray(array));

        array = new byte[4];
        getRandomAccessWriter().read(array);
        setFreeBlocks(FileSystemUtils.convertByteArray(array));

        array = new byte[30];
        getRandomAccessWriter().read(array);
        setFileListBitVector(new String(array));

        array = new byte[4];
        getRandomAccessWriter().read(array);
        setFreeFileIndex(FileSystemUtils.convertByteArray(array));

        array = new byte[4];
        getRandomAccessWriter().read(array);
        setFileCount(FileSystemUtils.convertByteArray(array));

    }

    private void addDirInfoToPfsFile() throws IOException {

        getRandomAccessWriter().seek(bitVector);
        getRandomAccessWriter().write(getBitVectorInBytes());
        getRandomAccessWriter().write(
                FileSystemUtils.convertIntToArrayBytes(getFreeStartingIndex()));//blockStartingIndex 4
        getRandomAccessWriter().write(
                FileSystemUtils.convertIntToArrayBytes(getFreeBlocks()));
        getRandomAccessWriter().write(getFileListBitVectorInBytes());
        getRandomAccessWriter().write(
                FileSystemUtils.convertIntToArrayBytes(getFreeFileIndex()));
        getRandomAccessWriter().write(
                FileSystemUtils.convertIntToArrayBytes(getFileCount()));


    }

    private void intializeAllAttributes() throws IOException {


        setFreeStartingIndex(0);

        setFreeBlocks(30);

        setBitVector(getInitData(mDiskBlocks));

        setFileListBitVector(getInitData(mDiskBlocks));

        setFileCount(30);

        setFreeFileIndex(0);

        addDirInfoToPfsFile();

        PfsFileManagementBlockModal emptyFileInfo = new PfsFileManagementBlockModal();

        for (int i = 0; i < mDiskBlocks; i++) {
            pfsWriteAttributes(i, emptyFileInfo);
        }

        //rndmAccess.seek(mDiskBlocks + 2);

    }

    private int getFileCount() {
        return noOfFiles;
    }

    private void setFileCount(int noOfFiles) {
        this.noOfFiles = noOfFiles;
    }

    private int getFreeFileIndex() {
        return freeDataBlockIndex;
    }

    private void setFreeFileIndex(int freeDataBlockIndex) {
        this.freeDataBlockIndex = freeDataBlockIndex;
    }

    private String getFileListBitVector() {
        return fileListBitVector;
    }

    private byte[] getFileListBitVectorInBytes() {
        return fileListBitVector.getBytes();
    }

    private void setFileListBitVector(String fileListBitVector) {
        this.fileListBitVector = fileListBitVector;
    }

    private boolean isFreeSpaceAvailable(int sizeInBytes) {
        if (sizeInBytes < getFreeSpaceAvailable()) {
            return true;
        } else
            return false;
    }

    private int getFreeSpaceAvailable() {
        return pfsFreeDataBlocks * maxAvail_dataBlockSize;
    }

    private String getBitVector() {
        return bitvector;
    }

    private byte[] getBitVectorInBytes() {
        return bitvector.getBytes();
    }

    private void setBitVector(String vector) {
        bitvector = vector;
    }

    /**
     * To get the block id which is free
     *
     * @param vector
     * @param freeIndex
     * @return
     */
    private int fetchNextFreeId(StringBuffer vector, int freeIndex) {
        for (int i = freeIndex + 1; i < mDiskBlocks; i++) {
            if (vector.charAt(i) == '0') {
                return i;//FreeIndex
            }
        }
        return -1;//something wrong happened
    }

    private RandomAccessFile getRandomAccessWriter() {
        return rndmAccess;
    }

    private int getFreeStartingIndex() {
        return blockStartingIndex;
    }

    private void setFreeStartingIndex(int blockStartingIndex) {
        this.blockStartingIndex = blockStartingIndex;
    }

    private int getFreeBlocks() {
        return pfsFreeDataBlocks;
    }

    private void setFreeBlocks(int pfsFreeDataBlocks) {
        this.pfsFreeDataBlocks = pfsFreeDataBlocks;
    }

    /**
     * To copy the file from the cuurent folder to the pfs file system
     *
     * @param fileName
     * @param sizeInBytes
     * @throws IOException
     */
    public void put(String fileName, int sizeInBytes) throws IOException {

        StringBuffer vector = new StringBuffer(getBitVector());

        int fileSystemIndex = getFreeFileIndex();

        if (fileSystemIndex == -1) {
            System.out.println("file system error-name: Maximum of 15 files can be added");
        } else if (isFreeSpaceAvailable(sizeInBytes)) {
            PfsFileManagementBlockModal newFileInfo = pfsAttributes[fileSystemIndex];
            int nextFreeIndex;
            int availableFreeBlocks;
            try (RandomAccessFile fileReader = new RandomAccessFile(fileName, "r")) {
                nextFreeIndex = getFreeStartingIndex();
                availableFreeBlocks = getFreeBlocks();
                newFileInfo.setFilename(new File(fileName).getName());
                newFileInfo.setCreateDate(new Date().toLocaleString());
                newFileInfo.setStartingDiskBlockID(nextFreeIndex);
                newFileInfo.setFileSize(sizeInBytes);
                while (sizeInBytes > 0) {
                    PfsDataBlockModal dataBlock = new PfsDataBlockModal(sizeInBytes > maxAvail_dataBlockSize ? maxAvail_dataBlockSize : sizeInBytes);
                    fileReader.read(dataBlock.getFileData());
                    sizeInBytes -= maxAvail_dataBlockSize;

                    getRandomAccessWriter().seek(mDirectoryInfo + nextFreeIndex * mDataBlockSize);

                    getRandomAccessWriter().write(dataBlock.getFileData());

                    getRandomAccessWriter().seek(mDirectoryInfo + nextFreeIndex * mDataBlockSize
                            + maxAvail_dataBlockSize);

                    vector.setCharAt(nextFreeIndex, '1');
                    if (sizeInBytes > 0) {
                        nextFreeIndex = fetchNextFreeId(vector, nextFreeIndex);
                        getRandomAccessWriter().write(
                                FileSystemUtils.convertIntToArrayBytes(nextFreeIndex));
                    } else {
                        getRandomAccessWriter().write(
                                FileSystemUtils.convertIntToArrayBytes(nextFreeIndex));
                    }
                    availableFreeBlocks--;
                }
            }

            newFileInfo.setEndingDiskBlockID(nextFreeIndex);


            setBitVector(vector.toString());

            setFreeStartingIndex(fetchNextFreeId(vector, nextFreeIndex));

            setFreeBlocks(availableFreeBlocks);

            StringBuffer pfsFileListVector = new StringBuffer(getFileListBitVector());
            pfsFileListVector.setCharAt(fileSystemIndex, '1');


            pfsWriteAttributes(fileSystemIndex, newFileInfo);


            setFreeFileIndex(getNextFreeFileIndex(pfsFileListVector, fileSystemIndex));

            setFileCount(noOfFiles - 1);

            setFileListBitVector(pfsFileListVector.toString());


            addDirInfoToPfsFile();

        } else {
            System.out.println("file system error-name:please provide less file size "
                    + getFreeSpaceAvailable() + " bytes"
            );
        }
    }

    private int getNextFreeFileIndex(StringBuffer pfsFileListVector, int fileSystemIndex) {
        for (int i = fileSystemIndex + 1; i < mDiskBlocks; i++) {
            if (pfsFileListVector.charAt(i) == '0') {
                return i;
            }
        }
        return -1;
    }

    public void get(String inputFileName) throws IOException {

        int lfi = findAttributes(inputFileName);
        PfsFileManagementBlockModal fileControlBlock = pfsAttributes[lfi];
        int begDiskBlock = fileControlBlock.getStartingDiskBlockID();
        int endDiskBlock = fileControlBlock.getEndingDiskBlockID();
        int lfiSize = fileControlBlock.getFileSize();
        PfsDataBlockModal blockToRead;
        byte[] tempBuf;

        if (lfi != -1) {
            FileSystemUtils.createDirectory("ext");
            try (FileOutputStream fos = new FileOutputStream(destDir + inputFileName)) {

                do {

                    blockToRead = new PfsDataBlockModal(lfiSize > maxAvail_dataBlockSize ? maxAvail_dataBlockSize : lfiSize);
                    tempBuf = new byte[4];
                    getRandomAccessWriter().seek(mDirectoryInfo + begDiskBlock * mDataBlockSize);
                    getRandomAccessWriter().read(blockToRead.getFileData());
                    fos.write(blockToRead.getFileData());
                    getRandomAccessWriter().seek(mDirectoryInfo + begDiskBlock * mDataBlockSize + maxAvail_dataBlockSize);
                    getRandomAccessWriter().read(tempBuf);
                    begDiskBlock = FileSystemUtils.convertByteArray(tempBuf);
                    lfiSize -= maxAvail_dataBlockSize;
                } while (begDiskBlock != endDiskBlock);
            }
        } else {
            System.out.println("OperationsOfPfsFile-ERROR: NO Such File Found in OperationsOfPfsFile");
        }
    }

    public void recover(String inputFileName) {
        //TODO
        int lfi = findAttributes(inputFileName);
        PfsFileManagementBlockModal fileControlBlock = pfsAttributes[lfi];
        if (lfi != -1)
            fileControlBlock.setDeleted(false);
        System.out.println("Successfully recovered file : " + inputFileName);
    }

    public void rm(String inputFileName) throws IOException {
        //TODO
        int lfi = findAttributes(inputFileName);
        PfsFileManagementBlockModal fileControlBlock = pfsAttributes[lfi];
        if (lfi != -1)
            fileControlBlock.setDeleted(true);
    }

    public void dir() throws IOException {
        int fileSystemIndex = getFreeFileIndex();
        int count = 0;

        for (int i = 0; i < fileSystemIndex; i++) {
            PfsFileManagementBlockModal preFileInfo = pfsAttributes[i];
            if (!preFileInfo.isDeleted()) {
                String name = preFileInfo.getFilename();
                int size = preFileInfo.getFileSize();
                String date = preFileInfo.getCreateDate();
                System.out.print(name);
                System.out.print(" | ");
                System.out.print(size);
                System.out.print("bytes ");
                System.out.print(" | ");
                System.out.print(date);
                System.out.print("\n");
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No file is available!");
            return;
        }
    }

    private int findAttributes(String inputFileName) {

        for (int i = 0; i < pfsAttributes.length; i++) {
            if (pfsAttributes[i].getFilename() == null) {
                System.out.println("File doesn't exist: " + inputFileName);
                return -1;
            }
            if (pfsAttributes[i].getFilename().equals(inputFileName)) {
                return i;
            }
        }
        return -1;
    }


    public void quitPfs() throws IOException {
        getRandomAccessWriter().close();
    }
}
