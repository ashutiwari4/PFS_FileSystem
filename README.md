# PFS_FileSystem


                            ▒█▀▀█ ▒█▀▀▀ ▒█▀▀▀█ 　 ▒█▀▀▀ ░▀░ █░░ █▀▀ █▀▀ █░░█ █▀▀ ▀▀█▀▀ █▀▀ █▀▄▀█
                            ▒█▄▄█ ▒█▀▀▀ ░▀▀▀▄▄ 　 ▒█▀▀▀ ▀█▀ █░░ █▀▀ ▀▀█ █▄▄█ ▀▀█ ░░█░░ █▀▀ █░▀░█
                            ▒█░░░ ▒█░░░ ▒█▄▄▄█ 　 ▒█░░░ ▀▀▀ ▀▀▀ ▀▀▀ ▀▀▀ ▄▄▄█ ▀▀▀ ░░▀░░ ▀▀▀ ▀░░░▀


READ ME FILE FOR "A PORTABLE FILE SYSTEM USING LINKED ALLOCATION METHOD"


Steps to execute the project in Linux Shell -</br>
1) Compile the project using the javac mFilename.java</br>
2) To execute the project use java mFilename</br>

The running programs accepts the following commands :</br>

1) open PFSfile - Allocates a new 10 KByte "PFS" file if it does not already exist. If it does exist, begin using it for further commands.</br>
2) put fileName - Copy file "fileName" into your PFS file.</br>
3) get fileName - Extract file "fileName" from your PFS file and copy it to the current filesystem directory.</br>
4) rm fileName - Delete "fileName" from your PFS file.</br>
5) recover filename - Recovers deleted file from your PFS file.</br>
5) dir - List the files in the PFS file.</br>
6) putr fileName "Remarks" - Append remarks to the directory entry for fileName in the PFS file.</br>
7) kill PFSfile - Delete the PFSfile from the file system.</br>
8) quit - Exit PFS.

*************** ------------------ ------------------- ^!^ ------------------ ------------------ ***************</br>

Limitations :</br>
1) This program is configured to show the operations on the Desktop.</br>
2) The file size should not exceed more than 10 bytes</br>
3) Keep in mind username should not be separated by space</br>
</br>
Assumptions :</br> 
1) Abstracting file system on an existing file system provided by the operating system. The file system implemented here is called portable file system.</br>
</br>
</br>


