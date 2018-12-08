package com.amaze.filemanager.utils;

import com.amaze.filemanager.filesystem.FileUtil;
import com.amaze.filemanager.filesystem.HybridFileParcelable;
import com.amaze.filemanager.filesystem.compressed.CompressedHelper;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class CreateFoldersTest{
    private HybridFileParcelable file;
    private HybridFileParcelable file2;
    private HybridFileParcelable file3;
    private HybridFileParcelable directory;
    private File zipfile1 = new File("/storage/sdcard0/Test/Test.zip");
    private File fileTest = new File("/storage/sdcard0/Test/TestFileTxt.txt");

    @Before
    public void createFile() {
        file = new HybridFileParcelable("/storage/sdcard0/Test/Test.txt",
                "rw", 146456, 654321, false);
        file2 = new HybridFileParcelable("/storage/sdcard0/Test/Test2.txt",
                "rwx", 146456, 654321, false);
        file3 = new HybridFileParcelable("/storage/sdcard0/Test/Test3.txt",
                "rx", 456321, 654321, false);
        directory = new HybridFileParcelable("/storage/sdcard0/Test2",
                "rw", 123456, 654321, true);
    }

    @Test
    public void testGetDate() {
        assertEquals(146456, file.getDate());
        assertEquals("Test.txt", file.getName());
    }

    @Test
    public void testValidFilename() {
        assertEquals(true,FileUtil.isValidFilename("TestFolder"));
        assertEquals(false,FileUtil.isValidFilename("?TestFolder"));
        assertEquals(false,FileUtil.isValidFilename(" "));
        assertEquals(false,FileUtil.isValidFilename("Test/1"));
        assertEquals(false,FileUtil.isValidFilename("Test:1"));
        assertEquals(true,FileUtil.isValidFilename("Test,2"));
        assertEquals(false,FileUtil.isValidFilename("Test*1"));
    }

    @Test
    public void testPermision() {
        assertEquals("rw",file.getPermission());
        assertEquals("rwx",file2.getPermission());
        assertEquals("rx",file3.getPermission());
    }

    @Test
    public void testIsDirectory() {
        assertFalse(file.isDirectory());
        assertTrue(directory.isDirectory());
        directory.setDirectory(false);
        assertFalse(directory.isDirectory());
    }

    @Test
    public void testIsFileExtractable() {
        //String type = getExtension(file.getPath());
        assertEquals(true,CompressedHelper.isFileExtractable(zipfile1.getPath()));
        assertEquals(false,CompressedHelper.isFileExtractable(file.getPath()));
    }
}