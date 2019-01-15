package io.jhdf.object.message;

import java.nio.ByteBuffer;
import java.util.BitSet;

import io.jhdf.Utils;
import io.jhdf.exceptions.HdfException;

/**
 * <p>
 * Group Info Message
 * </p>
 * 
 * <p>
 * <a href=
 * "https://support.hdfgroup.org/HDF5/doc/H5.format.html#GroupInfoMessage">Format
 * Spec</a>
 * </p>
 * 
 * @author James Mudd
 */
public class GroupInfoMessage extends Message {

	private static final int LINK_PHASE_CHANGE_PRESENT = 0;
	private static final int ESTIMATED_ENTRY_INFOMATION_PRESENT = 0;

	private final int maximumCompactLinks;
	private final int minimumDenseLinks;
	private final int estimatedNumberOfEntries;
	private final int estimatedLentghOfEntryName;

	/* package */ GroupInfoMessage(ByteBuffer bb, BitSet messageFlags) {
		super(messageFlags);

		final byte version = bb.get();
		if (version != 0) {
			throw new HdfException("Unreconised version " + version);
		}

		BitSet flags = BitSet.valueOf(new byte[] { bb.get() });

		if (flags.get(LINK_PHASE_CHANGE_PRESENT)) {
			maximumCompactLinks = Utils.readBytesAsUnsignedInt(bb, 2);
			minimumDenseLinks = Utils.readBytesAsUnsignedInt(bb, 2);
		} else {
			maximumCompactLinks = -1;
			minimumDenseLinks = -1;
		}

		if (flags.get(ESTIMATED_ENTRY_INFOMATION_PRESENT)) {
			estimatedNumberOfEntries = Utils.readBytesAsUnsignedInt(bb, 2);
			estimatedLentghOfEntryName = Utils.readBytesAsUnsignedInt(bb, 2);
		} else {
			estimatedNumberOfEntries = -1;
			estimatedLentghOfEntryName = -1;
		}
	}

	public int getMaximumCompactLinks() {
		return maximumCompactLinks;
	}

	public int getMinimumDenseLinks() {
		return minimumDenseLinks;
	}

	public int getEstimatedNumberOfEntries() {
		return estimatedNumberOfEntries;
	}

	public int getEstimatedLentghOfEntryName() {
		return estimatedLentghOfEntryName;
	}

}