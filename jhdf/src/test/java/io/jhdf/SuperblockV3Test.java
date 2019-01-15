package io.jhdf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jhdf.Superblock.SuperblockV2V3;
import io.jhdf.exceptions.HdfException;

public class SuperblockV3Test {
	private FileChannel fc;
	private RandomAccessFile raf;

	@BeforeEach
	public void setUp() throws FileNotFoundException {
		final String testFileUrl = this.getClass().getResource("test_file2.hdf5").getFile();
		raf = new RandomAccessFile(new File(testFileUrl), "r");
		fc = raf.getChannel();
	}

	@AfterEach
	public void after() throws IOException {
		raf.close();
		fc.close();
	}

	@Test
	public void testExtractV0SuperblockFromFile() throws IOException {
		Superblock sb = Superblock.readSuperblock(fc, 0);
		// Test version independent methods
		assertThat(sb.getVersionOfSuperblock(), is(equalTo(3)));
		assertThat(sb.getSizeOfOffsets(), is(equalTo(8)));
		assertThat(sb.getSizeOfLengths(), is(equalTo(8)));
		assertThat(sb.getBaseAddressByte(), is(equalTo(0L)));
		assertThat(sb.getEndOfFileAddress(), is(equalTo(raf.length())));

		// Test V3 only methods
		SuperblockV2V3 sbV3 = (SuperblockV2V3) sb;
		assertThat(sbV3.getSuperblockExtensionAddress(), is(equalTo(Constants.UNDEFINED_ADDRESS)));
		assertThat(sbV3.getRootGroupObjectHeaderAddress(), is(equalTo(48L)));
	}

	@Test
	public void testVerifySuperblock() throws Exception {
		assertThat(Superblock.verifySignature(fc, 0), is(true));
	}

	@Test
	public void testVerifySuperblockReturnsFalseWhenNotCorrect() throws Exception {
		assertThat(Superblock.verifySignature(fc, 3), is(false));
	}

	@Test
	public void testReadSuperblockThrowsWhenGivenInvalidOffset() throws Exception {
		assertThrows(HdfException.class, () -> Superblock.readSuperblock(fc, 5));
	}
}