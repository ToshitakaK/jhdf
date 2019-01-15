package io.jhdf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GroupSymbolTableNodeTest {
	private FileChannel fc;
	private RandomAccessFile raf;
	private Superblock sb;

	@BeforeEach
	public void setUp() throws FileNotFoundException {
		final String testFileUrl = this.getClass().getResource("test_file.hdf5").getFile();
		raf = new RandomAccessFile(new File(testFileUrl), "r");
		fc = raf.getChannel();
		sb = Superblock.readSuperblock(fc, 0);
	}

	@AfterEach
	public void after() throws IOException {
		raf.close();
		fc.close();
	}

	@Test
	public void testGroupSymbolTableNode() throws IOException {
		GroupSymbolTableNode node = new GroupSymbolTableNode(fc, 1504, sb);

		assertThat(node.getVersion(), is(equalTo((short) 1)));
		assertThat(node.getNumberOfEntries(), is(equalTo((short) 3)));
		assertThat(node.getSymbolTableEntries().length, is(equalTo(3)));
		assertThat(node.toString(), is(equalTo(
				"GroupSymbolTableNode [address=0x5e0, version=1, numberOfEntries=3, symbolTableEntries=[SymbolTableEntry [address=0x5e8, linkNameOffset=8, objectHeaderAddress=0x320, cacheType=1, bTreeAddress=0x348, nameHeapAddress=0x568, linkValueOffset=-1], SymbolTableEntry [address=0x610, linkNameOffset=24, objectHeaderAddress=0x3020, cacheType=1, bTreeAddress=0x3048, nameHeapAddress=0x3268, linkValueOffset=-1], SymbolTableEntry [address=0x638, linkNameOffset=40, objectHeaderAddress=0x3500, cacheType=1, bTreeAddress=0x3528, nameHeapAddress=0x3748, linkValueOffset=-1]]]")));
	}
}