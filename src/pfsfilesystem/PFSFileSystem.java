/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pfsfilesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 *
 * @author ashutosh
 */
public class PFSFileSystem {
    private fileSystem.OperationsOfPfsFile operationsOfPfsFile = null;

    /**
     * To accept the user command and parse it to the tokens
     *
     * @throws IOException
     */
    private void CommandParser() throws IOException {

        while (true) {

            String cmd1;
            String ip1 = null;
            String ip2 = null;
            System.out.print("pfs:~$ ");
            BufferedReader tempBuffer = new BufferedReader(new InputStreamReader(System.in));
            String userInput = tempBuffer.readLine();

            StringTokenizer commandTokens = new StringTokenizer(userInput, " ");
            if (!commandTokens.hasMoreTokens()) {
                continue;
            }
            cmd1 = commandTokens.nextToken();
            if (commandTokens.hasMoreTokens())
                ip1 = commandTokens.nextToken();
            if (commandTokens.hasMoreTokens())
                ip2 = commandTokens.nextToken();
            if (commandTokens.hasMoreTokens()) {
                System.out.println("file system error");
                continue;
            }
            validateCommands(cmd1, ip1, ip2);
        }
    }

    /**
     * To check the user command is valid or not and do the respective execution
     *
     * @param arg
     * @param ip1
     * @param ip2
     * @throws IOException
     */
    private void validateCommands(String arg, String ip1, String ip2) throws IOException {
        switch (arg.toLowerCase()) {
            case "open":
                if (ip1 != null && ip2 == null) {
                    File f1 = new File(ip1 + ".pfs");
                    if (f1.exists() && !f1.isDirectory()) {
                        operationsOfPfsFile = new fileSystem.OperationsOfPfsFile(ip1 + ".pfs");
                    } else if (!f1.exists()) {
                        System.out.println("file system error-name:  " + ip1);
                        operationsOfPfsFile = new fileSystem.OperationsOfPfsFile(ip1 + ".pfs");
                    }
                } else {
                    System.out.println("file system error:Enter C:\filename");
                }
                break;
            case "put":
                if (ip2 != null) {
                    System.out.println("file system error");
                } else if (ip1 != null) {
                    File f = new File(ip1);

                    if (!f.exists()) {
                        System.out.println("file system error- file doesn't exist");
                    } else if (f.getName().length() > 20) {
                        System.out.println("Make sure file name is less than 20 character");
                    } else {
                        operationsOfPfsFile.put(ip1, (int) f.length());
                    }
                }
                break;
            case "get":
                if (ip2 != null) {
                    System.out.println("file system error");
                } else if (ip1 != null) {
                    if (ip1.length() > 20) {
                        System.out.println("Make sure file name is less than 20 character");
                    } else {
                        operationsOfPfsFile.get(ip1);
                    }
                }
                break;
            case "dir":
                if (ip1 != null) {
                    System.out.println("file system error");
                } else {
                    operationsOfPfsFile.dir();
                }
                break;
            case "kill":
                operationsOfPfsFile.quitPfs();
                System.out.println("OperationsOfPfsFile system exited");
                break;
            case "quit":
                System.exit(0);
                break;
            case "rm":
                if (ip2 != null) {
                    System.out.println("file system error");
                } else if (ip1 != null) {
                    if (ip1.length() > 20) {
                        System.out.println("Make sure file name is less than 20 character");
                    } else {
                        operationsOfPfsFile.rm(ip1);
                    }
                }
                break;
            case "recover":
                if (ip2 != null) {
                    System.out.println("file system error");
                } else if (ip1 != null) {
                    if (ip1.length() > 20) {
                        System.out.println("Make sure file name is less than 20 character");
                    } else {
                        operationsOfPfsFile.recover(ip1);
                    }
                }
                break;
            default:
                System.out.println("file system error: Not a valid command");
                break;
        }
    }


    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        fileSystem.FileSystemUtils.createDirectory("PFS");
        PFSFileSystem pfsObj = new PFSFileSystem();
        pfsObj.CommandParser();
    }

}
