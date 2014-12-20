package jannovar.annotation.builders;

import jannovar.annotation.Annotation;
import jannovar.common.VariantType;
import jannovar.exception.InvalidGenomeChange;
import jannovar.io.ReferenceDictionary;
import jannovar.reference.GenomeChange;
import jannovar.reference.GenomePosition;
import jannovar.reference.HG19RefDictBuilder;
import jannovar.reference.PositionType;
import jannovar.reference.TranscriptInfo;
import jannovar.reference.TranscriptInfoBuilder;
import jannovar.reference.TranscriptModelFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

// TODO(holtgrem): Extend tests to also use reverse transcript?

public class BlockSubstitutionAnnotationBuilderTest {

	/** this test uses this static hg19 reference dictionary */
	static final ReferenceDictionary refDict = HG19RefDictBuilder.build();

	/** transcript on forward strand */
	TranscriptInfoBuilder builderForward;
	/** transcript on reverse strand */
	TranscriptInfoBuilder builderReverse;
	/** transcript info on forward strand */
	TranscriptInfo infoForward;
	/** transcript info on reverse strand */
	TranscriptInfo infoReverse;

	@Before
	public void setUp() {
		this.builderForward = TranscriptModelFactory
				.parseKnownGenesLine(
						refDict,
						"uc001anx.3	chr1	+	6640062	6649340	6640669	6649272	11	6640062,6640600,6642117,6645978,6646754,6647264,6647537,6648119,6648337,6648815,6648975,	6640196,6641359,6642359,6646090,6646847,6647351,6647692,6648256,6648502,6648904,6649340,	P10074	uc001anx.3");
		this.builderForward
				.setSequence("cgtcacgtccggcgcggagacggtggagtctccgcactgtcggcggggtacgcatagccgggcactaggttcgtgggctgtggaggcgacggagcagggggccagtggggccagctcagggaggacctgcctgggagctttctcttgcataccctcgcttaggctggccggggtgtcacttctgcctccctgccctccagaccatggacggctccttcgtccagcacagtgtgagggttctgcaggagctcaacaagcagcgggagaagggccagtactgcgacgccactctggacgtggggggcctggtgtttaaggcacactggagtgtccttgcctgctgcagtcactttttccagagcctctacggggatggctcagggggcagtgtcgtcctccctgctggcttcgctgagatctttggcctcttgttggactttttctacactggtcacctcgctctcacctcagggaaccgggatcaggtgctcctggcagccagggagttgcgagtgccagaggccgtagagctgtgccagagcttcaagcccaaaacttcagtgggacaggcagcaggtggccagagtgggctggggccccctgcctcccagaatgtgaacagccacgtcaaggagccggcaggcttggaagaagaggaagtttcgaggactctgggtctagtccccagggatcaggagcccagaggcagtcatagtcctcagaggccccagctccattccccagctcagagtgagggcccctcctccctctgtgggaaactgaagcaggccttgaagccttgtccccttgaggacaagaaacccgaggactgcaaagtgcccccaaggcccttagaggctgaaggtgcccagctgcagggcggcagtaatgagtgggaagtggtggttcaagtggaggatgatggggatggcgattacatgtctgagcctgaggctgtgctgaccaggaggaagtcaaatgtaatccgaaagccctgtgcagctgagccagccctgagcgcgggctccctagcagctgagcctgctgagaacagaaaaggtacagcggtgccggtcgaatgccccacatgtcataaaaagttcctcagcaaatattatctaaaagtccacaacaggaaacatactggggagaaaccctttgagtgtcccaaatgtgggaagtgttactttcggaaggagaacctcctggagcatgaagcccggaattgcatgaaccgctcggaacaggtcttcacgtgctctgtgtgccaggagacattccgccgaaggatggagctgcgggtgcacatggtgtctcacacaggggagatgccctacaagtgttcctcctgctcccagcagttcatgcagaagaaggacttgcagagccacatgatcaaacttcatggagcccccaagccccatgcatgccccacctgtgccaagtgcttcctgtctcggacagagctgcagctgcatgaagctttcaagcaccgtggtgagaagctgtttgtgtgtgaggagtgtgggcaccgggcctcgagccggaatggcctgcagatgcacatcaaggccaagcacaggaatgagaggccacacgtatgtgagttctgcagccacgccttcacccaaaaggccaatctcaacatgcacctgcgcacacacacgggtgagaagcccttccagtgccacctctgtggcaagaccttccgaacccaagccagcctggacaagcacaaccgcacccacaccggggaaaggcccttcagttgcgagttctgtgaacagcgcttcactgagaaggggcccctcctgaggcacgtggccagccgccatcaggagggccggccccacttctgccagatatgcggcaagaccttcaaagccgtggagcaactgcgtgtgcacgtcagacggcacaagggggtgaggaagtttgagtgcaccgagtgtggctacaagtttacccgacaggcccacctgcggaggcacatggagatccacgaccgggtagagaactacaacccgcggcagcgcaagctccgcaacctgatcatcgaggacgagaagatggtggtggtggcgctgcagccgcctgcagagctggaggtgggctcggcggaggtcattgtggagtccctggcccagggcggcctggcctcccagctccccggccagagactgtgtgcagaggagagcttcaccggcccaggtgtcctggagccctccctcatcatcacagctgctgtccccgaggactgtgacacatagcccattctggccaccagagcccacttggccccacccctcaataaaccgtgtggctttggactctcgtaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
						.toUpperCase());
		this.builderForward.setGeneSymbol("ZBTB48");
		this.infoForward = builderForward.build();
		// RefSeq: NM_005341.3

		this.builderReverse = TranscriptModelFactory
				.parseKnownGenesLine(
						refDict,
						"uc001bgu.3	chr1	-	23685940	23696357	23688461	23694498	4	23685940,23693534,23694465,23695858,	23689714,23693661,23694558,23696357,	Q9C0F3	uc001bgu.3");
		this.builderReverse
				.setSequence("aataagctgctatattctttttccatcacttccctctccaaggctacagcgagctgggagctcttccccacgcagaatgcctgctttccccagtgctcgacttccattgtctaattccctcatcctggctggggaaagggagagctgcgagtcctcccgttccgaggaactccagctgaatgcagcttagttgctggtggtttctcggccagcctctgtggtctcagggatctgcctatgagcctgtggtttctgagctgcctgcgagtctgaggcctcgggaatctgagtctttaggatcagcctacgatatctgggcttcgcctgcaagtctacgaattcgagatctacctgcgggtctgagacctccgggacctgcccgtgctctctagaatcttcctgaacgccaggtctgagagaacgctgcggctctggaacccgttcgcggtctctcaggttttggagacgacgatctagtggatcttttgcgggacaggagcgctgtctgctagctgcttttcctgctctctctccctggaggcgaacccttgtgctcgagatggcagccaccctgctcatggctgggtcccaggcacctgtgacgtttgaagatatggccatgtatctcacccgggaagaatggagacctctggacgctgcacagagggacctttaccgggatgttatgcaggagaattatggaaatgttgtctcactagattttgagatcaggagtgagaacgaggtaaatcccaagcaagagattagtgaagatgtacaatttgggactacatctgaaagacctgctgagaatgctgaggaaaatcctgaaagtgaagagggctttgaaagcggagataggtcagaaagacaatggggagatttaacagcagaagagtgggtaagctatcctctccaaccagtcactgatctacttgtccacaaagaagtccacacaggcatccgctatcatatatgttctcattgtggaaaggccttcagtcagatctcagaccttaatcgacatcagaagacccacactggagacagaccctataaatgttatgaatgtggaaaaggcttcagtcgcagctcacaccttattcagcatcaaagaacacatactggggagaggccttatgactgtaacgagtgtgggaaaagttttggaagaagttctcacctgattcagcatcagacaatccacactggagagaagcctcacaaatgtaatgagtgtggaaaaagtttctgccgtctctctcacctaatccaacaccaaaggacccacagtggtgagaaaccctatgagtgtgaggagtgtgggaaaagcttcagccggagctctcacctagctcagcaccagaggacccacacgggtgagaaaccttatgaatgtaacgaatgtggccgaggcttcagtgagagatctgatctcatcaaacactatcgagtccacacaggggagaggccctacaagtgtgatgagtgtgggaagaatttcagtcagaactccgaccttgtgcgtcatcgcagagcccacacgggagagaagccataccactgtaacgaatgtggggaaaatttcagccgcatctcacacttggttcagcaccagagaactcacactggagagaagccatatgaatgcaatgcttgtgggaaaagcttcagccggagctctcatctcatcacacaccagaaaattcacactggagagaagccttatgagtgtaatgagtgttggcgaagctttggtgaaaggtcagatctaattaaacatcagagaacccacacaggggagaagccctacgagtgtgtgcagtgtgggaaaggtttcacccagagctccaacctcatcacacatcaaagagttcacacgggagagaaaccttatgaatgtaccgaatgtgagaagagtttcagcaggagctcagctcttattaaacataagagagttcatacggactaagctgtaattatgatggctgagaaatgattcatttgaagatacaattttatttgatatcaatgaacgccctcaagactgagctgcttttatcatactctcctagttgtgggccacgatttaaaccatcagagatgacaagccatttgaaattctgaccctcagctttgggaatgttatctcctccaaaatggtgatttttattcactcaatgggttacttcattaaaagcagccccacaagtaactggaaatctgaagaccaggggacaaatgctggtgaatgcttaggcctggaaatggagtaaatctttcaatgttattttctcccatccttggcccaaggaactatgctaagtgaaacgtgggactgtaatagggtggtaatggctgctttggaaaaaggcaactagagactctgcctaaattgccacacctattcacacaccatagtagttgggcacacacatcttcccttccaaagggctttttccttgagttgctcatgcatttgtatcttttccatcttcctgagggcaagattttgcacgatgaaggcaatgattgtaacttttctccttctcattgtttctaattagctcctttaaagcttgcatctttgtgaaggctaactgaagatacggttggaaaggaaaaatgagacacaggtttggggaccaaggacccatcaatgatggtgactttagcagaagatgcccacagttattactgccattaatcagatttatgaattttctttggggatcactatagggaatattgtatagaaaatatcttcaagaaaagataggaccatcagtgacagttaagtgtaaggagcaagtggaattgagtccttcagggaaggaaccacagagtcccttcccaaggaatgtaggtcgtttctgtgttctttcccttctaatctttaagatcaactcttcctatcctgctaactctaagatttgataagggccacatcccagtgtttatcttagcttgcatcagggcatgtgtatgtacagtaatgtgtattcctgtggtttttctaatagaaactgaatttacagagacttagcatgttcttgggtgatgtgagtcatgtgacagaagtacagacataactccaatgtgagaaatgtccttttttcattatggaaaataatttaaacactagtgctttagtgtgcactctcctgtaaggtctgtctttgtacagagctaagcacttgtttgtatgtgtttgtcaattgtggaagataatgaccagacaaataggtcgattgtcctattctcagaatgaattatcttctatggtaatgaagaactctttggcttagtcagaaggaattaacgaacctcggtaggaatgtatttccatcctcccaccctacagatataagaggttaaaataacagttcgcccaatttaagcccagtagtgtcagttttcctaatctcagtccaggtaggaattaagaaatatctcaagtgttgatgctatccaagcatgttggggtggaagggaattggtgcccagaaaatgggactggagtgaggaatatcttttcttttgagagtacccccagtttatttctactgtgctttattgctactgttctttattgtgaatgttgtaacattttaaaaatgttttgccatagctttttaggacttggtgttaaaggagccagtggtctctctgggtgggtactataatgagttattgtgacccacagctgtgtgggaccacatcacttgttaataacacaacctttaaagtaacccatcttccaggggggttccttcatgttgccactcctttttaaggacaaactcaggcaaggagcatgtttttttgttatttacaaaatctagcagactgtgggtatccatattttaattgtcgggtgacacatgttcttggtaactaaactcaaatatgtcttttctcatatatgttgctgatggttttaataaatgtcaaagttctcctgttgcttctgtgagccactatgggtatcagcttgggagtggccatagatgaccgcatttccatgacctaactgtatttcacccccttttccttccctactgttcttgccccaccccaaccagttcctgctgctgcttttggcttcttggaggtgaagggcttaaaacaaggcttctaagcacccagctatctccatacatgaacaatctagctgggaaacttaagggacaagggccacaccagctgtctcctctttctgccaattgttgcccgtttgctgtgttgaactttgtatagaactcatgcatcagactcccttcactaatgctttttgcatgccttctgctcccaagtccctggctgcctctgcacatcccgtgaacactttgtgcctgttttctatggttgtggagaattaatgaacaaatcaatatgtagaacagttttccttatggtattggtcacagttatcctagtgtttgtattattctaacaatattctataattaaaaatataatttttaaagtca"
						.toUpperCase());
		this.builderReverse.setGeneSymbol("ZNF436");
		this.infoReverse = builderReverse.build();
		// RefSeq: NM_001077195.1
	}

	@Test
	public void testForwardUstream() throws InvalidGenomeChange {
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6640059, PositionType.ZERO_BASED),
				"ACG", "CGTT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("dist=0", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.UPSTREAM, annotation1.getVariantType());
	}

	@Test
	public void testForwardDownstream() throws InvalidGenomeChange {
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6649340, PositionType.ZERO_BASED),
				"ACG", "CGTT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("dist=0", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.DOWNSTREAM, annotation1.getVariantType());
	}

	@Test
	public void testForwardIntergenic() throws InvalidGenomeChange {
		// intergenic upstream
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6639059, PositionType.ZERO_BASED),
				"ACG", "CGTT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("dist=1000", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.INTERGENIC, annotation1.getVariantType());
		// intergenic downstream
		GenomeChange change2 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6650340, PositionType.ZERO_BASED),
				"ACG", "CGTT");
		Annotation annotation2 = new BlockSubstitutionAnnotationBuilder(infoForward, change2).build();
		Assert.assertEquals("dist=1000", annotation2.getVariantAnnotation());
		Assert.assertEquals(VariantType.INTERGENIC, annotation2.getVariantType());
	}

	@Test
	public void testForwardTranscriptAblation() throws InvalidGenomeChange {
		StringBuilder chars200 = new StringBuilder();
		for (int i = 0; i < 200; ++i)
			chars200.append("A");
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6640061, PositionType.ZERO_BASED),
				chars200.toString(), "CGTT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001anx.3:c.-204_-70+65delinsCGTT", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.TRANSCRIPT_ABLATION, annotation1.getVariantType());
	}

	@Test
	public void testForwardIntronic() throws InvalidGenomeChange {
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6642106, PositionType.ZERO_BASED),
				"ACG", "CGTT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001anx.3:c.691-11_691-9delinsCGTT", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.INTRONIC, annotation1.getVariantType());
	}

	@Test
	public void testForwardFivePrimeUTR() throws InvalidGenomeChange {
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6640070, PositionType.ZERO_BASED),
				"ACG", "CGTT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001anx.3:exon1:c.-195_-193delinsCGTT", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.UTR5, annotation1.getVariantType());
	}

	@Test
	public void testForwardThreePrimeUTR() throws InvalidGenomeChange {
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6649329, PositionType.ZERO_BASED),
				"ACG", "CGGTT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001anx.3:exon11:c.*58_*60delinsCGGTT", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.UTR3, annotation1.getVariantType());
	}

	@Test
	public void testForwardStartLoss() throws InvalidGenomeChange {
		// Testing with some START_LOSS scenarios.

		// Delete one base of start codon.
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6640669, PositionType.ZERO_BASED),
				"ACG", "CGTT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001anx.3:exon2:c.1_3delinsCGTT:p.0?", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.START_LOSS, annotation1.getVariantType());

		// Delete chunk out of first exon, spanning start codon from the left.
		GenomeChange change2 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6640660, PositionType.ZERO_BASED),
				"CCCTCCAGACC", "GTTG");
		Annotation annotation2 = new BlockSubstitutionAnnotationBuilder(infoForward, change2).build();
		Assert.assertEquals("uc001anx.3:exon2:c.-9_2delinsGTTG:p.0?", annotation2.getVariantAnnotation());
		Assert.assertEquals(VariantType.START_LOSS, annotation2.getVariantType());

		// Delete chunk out of first exon, spanning start codon from the right.
		GenomeChange change3 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6640671, PositionType.ZERO_BASED),
				"GGACGGCTCCT", "CTTG");
		Annotation annotation3 = new BlockSubstitutionAnnotationBuilder(infoForward, change3).build();
		Assert.assertEquals("uc001anx.3:exon2:c.3_13delinsCTTG:p.0?", annotation3.getVariantAnnotation());
		Assert.assertEquals(VariantType.START_LOSS, annotation3.getVariantType());

		// Deletion from before transcript, reaching into the start codon.
		GenomeChange change4 = new GenomeChange(
				new GenomePosition(refDict, '+', 1, 6640399, PositionType.ZERO_BASED),
				"TCTCACCAGGCCCTTCTTCACGACCCTGGCCCCCCATCCAGCATCCCCCCTGGCCAATCCAATATGGCCCCCGGCCCCCGGGAGGCTGTCAGTGTGTTCCAGCCCTCCGCGTGCACCCCTCACCCTGACCCAAGCCCTCGTGCTGATAAATATGATTATTTGAGTAGAGGCCAACTTCCCGTTTCTCTCTCTTGACTCCAGGAGCTTTCTCTTGCATACCCTCGCTTAGGCTGGCCGGGGTGTCACTTCTGCCTCCCTGCCCTCCAGACCA",
				"ACCT");
		Annotation annotation4 = new BlockSubstitutionAnnotationBuilder(infoForward, change4).build();
		Assert.assertEquals("uc001anx.3:c.-69-201_1delinsACCT:p.0?", annotation4.getVariantAnnotation());
		Assert.assertEquals(VariantType.START_LOSS, annotation4.getVariantType());
	}

	@Test
	public void testForwardStopLoss() throws InvalidGenomeChange {
		// Replace bases of stop codon by 4 nucleotides, frameshift case.
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6649271, PositionType.ZERO_BASED),
				"ACG", "CGTT");
		// Note that the transcript here differs to the one Mutalyzer uses after the CDS.
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001anx.3:exon11:c.2067_*2delinsCGTT:p.*689Tyrext*25", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.STOPLOSS, annotation1.getVariantType());

		// Replace stop codon by 6 nucleotides, non-frameshift case.
		GenomeChange change2 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6649270, PositionType.ZERO_BASED),
				"ACT", "CGGTCG");
		Annotation annotation2 = new BlockSubstitutionAnnotationBuilder(infoForward, change2).build();
		// Note that the transcript here differs to the one Mutalyzer uses after the CDS.
		Assert.assertEquals("uc001anx.3:exon11:c.2066_*1delinsCGGTCG:p.*689Serext*17",
				annotation2.getVariantAnnotation());
		Assert.assertEquals(VariantType.STOPLOSS, annotation2.getVariantType());

		// Delete first base of stop codon, leads to complete loss.
		GenomeChange change3 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6649269, PositionType.ZERO_BASED),
				"ACG", "CGGT");
		Annotation annotation3 = new BlockSubstitutionAnnotationBuilder(infoForward, change3).build();
		// Note that the transcript here differs to the one Mutalyzer uses after the CDS.
		Assert.assertEquals("uc001anx.3:exon11:c.2065_2067delinsCGGT:p.*689Argext*16",
				annotation3.getVariantAnnotation());
		Assert.assertEquals(VariantType.STOPLOSS, annotation3.getVariantType());
	}

	@Test
	public void testForwardSplicing() throws InvalidGenomeChange {
		// intronic splicing
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6642116, PositionType.ZERO_BASED),
				"G", "TT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001anx.3:c.691-1delinsTT", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.SPLICE_ACCEPTOR, annotation1.getVariantType());

		// exonic splicing
		GenomeChange change2 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6642117, PositionType.ZERO_BASED),
				"TGG", "AA");
		Annotation annotation2 = new BlockSubstitutionAnnotationBuilder(infoForward, change2).build();
		Assert.assertEquals("uc001anx.3:exon3:c.691_693delinsAA:p.Trp231Lysfs*23", annotation2.getVariantAnnotation());
		Assert.assertEquals(VariantType.SPLICE_REGION, annotation2.getVariantType());
	}

	@Test
	public void testForwardFrameShiftBlockSubstitution() throws InvalidGenomeChange {
		// The following case contains a shift in the nucleotide sequence.
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6647537, PositionType.ZERO_BASED),
				"TGCCCCACCT", "CCC");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001anx.3:exon7:c.1225_1234delinsCCC:p.Cys409Profs*127",
				annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.SPLICE_REGION, annotation1.getVariantType());
	}

	@Test
	public void testForwardNonFrameBlockSubstitution() throws InvalidGenomeChange {
		// deletion of two codons, insertion of one
		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6642114, PositionType.ZERO_BASED),
				"TAAACA", "GTT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001anx.3:c.691-3_693delinsGTT:p.Trp231Val", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.SPLICE_ACCEPTOR, annotation1.getVariantType());

		// deletion of three codons, insertion of one
		GenomeChange change2 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6642126, PositionType.ZERO_BASED),
				"GTGGTTCAA", "ACC");
		Annotation annotation2 = new BlockSubstitutionAnnotationBuilder(infoForward, change2).build();
		Assert.assertEquals("uc001anx.3:exon3:c.700_708delinsACC:p.Val234_Gln236delinsThr",
				annotation2.getVariantAnnotation());
		Assert.assertEquals(VariantType.NON_FS_SUBSTITUTION, annotation2.getVariantType());

		// deletion of three codons, insertion of one, includes truncation of replacement ref from the right
		GenomeChange change3 = new GenomeChange(new GenomePosition(refDict, '+', 1, 6642134, PositionType.ZERO_BASED),
				"AGTGGAGGAT", "CTT");
		Annotation annotation3 = new BlockSubstitutionAnnotationBuilder(infoForward, change3).build();
		Assert.assertEquals("uc001anx.3:exon3:c.708_716delinsCT:p.Gln236Hisfs*16", annotation3.getVariantAnnotation());
		Assert.assertEquals(VariantType.FS_SUBSTITUTION, annotation3.getVariantType());
	}

	@Test
	public void testRealWorldCase_uc002djq_3() throws InvalidGenomeChange {
		this.builderForward = TranscriptModelFactory
				.parseKnownGenesLine(
						refDict,
						"uc002axo.4	chr15	+	74528629	74628482	74529060	74628394	19	74528629,74536325,74554780,74559018,74560682,74564043,74565111,74572303,74573008,74574118,74588094,74622529,74623003,74623321,74623543,74625019,74626221,74627315,74628265,	74529081,74536489,74554914,74559128,74560799,74564135,74565232,74572433,74573142,74574190,74588289,74622695,74623092,74623453,74623637,74625186,74626308,74627429,74628482,	Q8N5R6-6	uc002axo.4");
		this.builderForward
				.setSequence("agtgttgcccaggtgacccagctaggtggaagagcttattgttttccaggcctgggcagagacagggcccccctgccccatctctccaccgtcctaggtgtgccaagagtcaattgcctcattgctgaccctgtccagctggccatggccctcaacccccaaggcccttccacccacagaccatctcgctgctgagagatccaggacctgctcccacctggccaccctccccctccccccacatccaggccccagggctggtgtgtggcacccctgagaccacattgacctccatactgtctactacccataaggactccaagacgcccaggccagctgtctgggcaggactgattcctgatcacccactgataccaagtactcatccccaagattgttaaacaaggccagacactcctggcctcaagaggatgggactgaaaaacaaaaagaacactgaagacccagaggagcccctgatcgcctcccagagcacggaacctgagatcggtcacctgtctccctctaagaaggagaccatcatggtcaccctccatggggctaccaacctgcctgcctgcaaggatggctccgagccgtggccctatgtggtggtgaaaagcacatctgaggaaaagaacaatcagagctccaaggcagtcacatctgtgacctcagagcccaccagagcccctatctggggggacacggtgaatgtggagatccaagctgaggatgcagggcaagaagatgtgatcctcaaggtggtggacaacagaaagaaacaggagttgttgtcctacaaaatccccatcaagtacctgcgtgtcttccacccctaccactttgagctggtgaagcccactgagtctgggaaagccgatgaagccactgccaagacccagttgtacgcaacagtcgttcggaagagcagcttcataccccgctacatcggctgcaaccacatggctctggagatctttctccggggagtcaacgagcccctggccaacaaccccaaccccatagtggtgattgcccgggtcgttcccaactacaaggaatttaaggtcagccaggctaacagggacctggcctctgtggggctgcccatcaccccactgtccttccctatcccgtccatgatgaactttgacgtgcctcgcgtcagccagaacggatgccctcagctgtccaagcctgggggacccccagagcagcccctgtggaatcagtccttcctcttccaaggccgagatggagctaccagcttctcagaagacacagccctggtgctggagtactactcctcaacttcaatgaaaggcagccagccgtggaccctcaaccagcccctgggcatctctgtgttgccgctaaagagccgtttgtaccagaagatgctgacagggaaaggcttggacgggcttcacgtggagcggctccccatcatggacaccagcctgaaaactatcaatgatgaggcccccacagtggctctctccttccagctgctttcctctgagagaccagaaaacttcttgacaccaaacaacagcaaggctcttcctaccttggaccccaagatcctggataagaagctgagaaccatccaagagtcctggtccaaggacacagtgagctccacaatggacttgagcacgtccactccacgagaagcagaggaggaacctctggtgcctgagatgtcccatgacacagagatgaacaactaccggcgggccatgcagaagatggcagaggacatcctgtctctgcggagacaggccagcatcctggaaggagagaaccgcatactgaggagccgcctggcccagcaggaggaggaagaggggcagggcaaagccagtgaggcccagaacacggtgtccatgaagcagaaactgctgctgagtgagctggatatgaagaaactgagggacagggtgcagcatttgcagaatgagctgattcgaaagaatgatcgagagaaggagctgctccttctgtatcaggcccagcagccacaggccgctctgctgaagcagtaccagggcaagctgcagaagatgaaggcgctggaggagactgtgcggcaccaagagaaggtgatcgagaagatggagcgggtgctggaggacaggctgcaggacaggagcaagccccctcctctgaacaggcagcagggaaagccctacacgggcttccctatgctctcagcctctggccttcccttgggttctatgggagagaacctgccggttgaactttactcggtgctgctggcagaaaacgcgaagctgcggacggagctggataagaaccgccaccagcaggcccccatcattctgcagcaacaggccctgccggatctcctctctggtacttcagacaagttcaacctcctggccaagctggaacacgctcagagccggatcctgtccctggaaagccagttagaggactcagctcgacgctggggacgagagaagcaggatctggccacacggctgcaggagcaagaaaaaggtttcaggcacccctcgaactccatcatcatagaacagcctagtgccctcacccactccatggacctcaagcagccctcagagctggagcccctgctgcccagctcagactctaagctcaacaagcccttgagcccccagaaggagaccgctaactctcagcagacctgagccccagagcaggcctccttccctgtgtgctggggagtctcatcaccgccccctaaaaatgacgttattaaatgttgtagctctgtgaaaaaaaaaaaa"
						.toUpperCase());
		this.builderForward.setGeneSymbol("CCDC33");
		this.infoForward = builderForward.build();
		// RefSeq NM_025055.4

		GenomeChange change1 = new GenomeChange(
				new GenomePosition(refDict, '+', 15, 74536399, PositionType.ZERO_BASED), "TAAGAAGGAGACCATCA",
				"ACTACCAGAGGAAT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc002axo.4:exon2:c.96_112delinsACTACCAGAGGAAT:p.Lys33_Met38delinsLeuProGluGluLeu",
				annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.NON_FS_SUBSTITUTION, annotation1.getVariantType());
	}

	@Test
	public void testRealWorldCase_uc010qzf_2() throws InvalidGenomeChange {
		this.builderForward = TranscriptModelFactory.parseKnownGenesLine(refDict,
				"uc010qzf.2	chr11	+	5474637	5475707	5474718	5475657	1	5474637,	5475707,	Q9H344	uc010qzf.2");
		this.builderForward
				.setSequence("ttgtcctccagcaagtgcaactgttagaattctccaagtcagaagatctgactctgaaaagtaccctaagtttgttttgctatggggttgttcaatgtcactcaccctgcattcttcctcctgactggtatccctggtctggagagctctcactcctggctgtcagggcccctctgcgtgatgtatgctgtggcccttgggggaaatacagtgatcctgcaggctgtgcgagtggagcccagcctccatgagcccatgtactacttcctgtccatgttgtccttcagtgatgtggccatatccatggccacactgcccactgtactccgaaccttctgcctcaatgcccgcaacatcacttttgatgcctgtctaattcagatgtttcttattcacttcttctccatgatggaatcaggtattctgctggccatgagttttgaccgctatgtggccatttgtgaccccttgcgctatgcaactgtgctcaccactgaagtcattgctgcaatgggtttaggtgcagctgctcgaagcttcatcacccttttccctcttccctttcttattaagaggctgcctatctgcagatccaatgttctttctcactcctactgcctgcacccagacatgatgaggcttgcctgtgctgatatcagtatcaacagcatctatggactctttgttcttgtatccacctttggcatggacctgttttttatcttcctctcctatgtgctcattctgcgttctgtcatggccactgcttcccgtgaggaacgcctcaaagctctcaacacatgtgtgtcacatatcctggctgtacttgcattttatgtgccaatgattggggtctccacagtgcaccgctttgggaagcatgtcccatgctacatacatgtcctcatgtcaaatgtgtacctatttgtgcctcctgtgctcaaccctctcatttatagcgccaagacaaaggaaatccgccgagccattttccgcatgtttcaccacatcaaaatatgactttcacacttggctttagaatctgttattttggccataggctctcatca"
						.toUpperCase());
		this.builderForward.setGeneSymbol("LOC100132247");
		this.infoForward = builderForward.build();
		// RefSeq NM_001004754.2

		GenomeChange change1 = new GenomeChange(new GenomePosition(refDict, '+', 11, 5475430, PositionType.ZERO_BASED),
				"TCAACA", "ACAACACT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc010qzf.2:exon1:c.713_718delinsACAACACT:p.Leu238Hisfs*19",
				annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.FS_SUBSTITUTION, annotation1.getVariantType());
	}

	@Test
	public void testRealWorldCase_uc011ddm_2_first() throws InvalidGenomeChange {
		this.builderForward = TranscriptModelFactory
				.parseKnownGenesLine(
						refDict,
						"uc011ddm.2	chr5	-	156479371	156485970	156479372	156484954	4	156479371,156482211,156484908,156485931,	156479665,156482544,156485087,156485970,	E9PFX0	uc011ddm.2");
		this.builderForward
				.setSequence("gttacccagcattgtgagtgacagagcctggatctgaacagcaggctcatatgaatcaaccaactgggtgaaaagataagttgcaatctgagatttaagacttgatcagataccatctggtggagggtaccaaccagcctgtctgctcattttccttcaggctgatcccataatgcatcctcaagtggtcatcttaagcctcatcctacatctggcagattctgtagctggttctgtaaaggttggtggagaggcaggtccatctgtcacactaccctgccactacagtggagctgtcacatccatgtgctggaatagaggctcatgttctctattcacatgccaaaatggcattgtctggaccaatggaacccacgtcacctatcggaaggacacacgctataagctattgggggacctttcaagaagggatgtctctttgaccatagaaaatacagctgtgtctgacagtggcgtatattgttgccgtgttgagcaccgtgggtggttcaatgacatgaaaatcaccgtatcattggagattgtgccacccaaggtcacgactactccaattgtcacaactgttccaaccgtcacgactgttcgaacgagcaccactgttccaacgacaacgactgttccaatgacgactgttccaacgacaactgttccaacaacaatgagcattccaacgacaacgactgttctgacgacaatgactgtttcaacgacaacgagcgttccaacgacaacgagcattccaacaacaacaagtgttccagtgacaacaactgtctctacctttgttcctccaatgcctttgcccaggcagaaccatgaaccag"
						.toUpperCase());
		this.builderForward.setGeneSymbol("HAVCR1");
		this.infoForward = builderForward.build();
		// RefSeq NM_012206.2

		GenomeChange change1 = new GenomeChange(
				new GenomePosition(refDict, '+', 5, 156479564, PositionType.ZERO_BASED), "AGTCGT", "AGTGAG");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc011ddm.2:exon4:c.475_477delinsCTC:p.Thr159Leu", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.NON_FS_SUBSTITUTION, annotation1.getVariantType());
	}

	@Test
	public void testRealWorldCase_uc002axo_4() throws InvalidGenomeChange {
		this.builderForward = TranscriptModelFactory
				.parseKnownGenesLine(
						refDict,
						"uc002axo.4	chr15	+	74528629	74628482	74529060	74628394	19	74528629,74536325,74554780,74559018,74560682,74564043,74565111,74572303,74573008,74574118,74588094,74622529,74623003,74623321,74623543,74625019,74626221,74627315,74628265,	74529081,74536489,74554914,74559128,74560799,74564135,74565232,74572433,74573142,74574190,74588289,74622695,74623092,74623453,74623637,74625186,74626308,74627429,74628482,	Q8N5R6-6	uc002axo.4");
		this.builderForward
				.setSequence("agtgttgcccaggtgacccagctaggtggaagagcttattgttttccaggcctgggcagagacagggcccccctgccccatctctccaccgtcctaggtgtgccaagagtcaattgcctcattgctgaccctgtccagctggccatggccctcaacccccaaggcccttccacccacagaccatctcgctgctgagagatccaggacctgctcccacctggccaccctccccctccccccacatccaggccccagggctggtgtgtggcacccctgagaccacattgacctccatactgtctactacccataaggactccaagacgcccaggccagctgtctgggcaggactgattcctgatcacccactgataccaagtactcatccccaagattgttaaacaaggccagacactcctggcctcaagaggatgggactgaaaaacaaaaagaacactgaagacccagaggagcccctgatcgcctcccagagcacggaacctgagatcggtcacctgtctccctctaagaaggagaccatcatggtcaccctccatggggctaccaacctgcctgcctgcaaggatggctccgagccgtggccctatgtggtggtgaaaagcacatctgaggaaaagaacaatcagagctccaaggcagtcacatctgtgacctcagagcccaccagagcccctatctggggggacacggtgaatgtggagatccaagctgaggatgcagggcaagaagatgtgatcctcaaggtggtggacaacagaaagaaacaggagttgttgtcctacaaaatccccatcaagtacctgcgtgtcttccacccctaccactttgagctggtgaagcccactgagtctgggaaagccgatgaagccactgccaagacccagttgtacgcaacagtcgttcggaagagcagcttcataccccgctacatcggctgcaaccacatggctctggagatctttctccggggagtcaacgagcccctggccaacaaccccaaccccatagtggtgattgcccgggtcgttcccaactacaaggaatttaaggtcagccaggctaacagggacctggcctctgtggggctgcccatcaccccactgtccttccctatcccgtccatgatgaactttgacgtgcctcgcgtcagccagaacggatgccctcagctgtccaagcctgggggacccccagagcagcccctgtggaatcagtccttcctcttccaaggccgagatggagctaccagcttctcagaagacacagccctggtgctggagtactactcctcaacttcaatgaaaggcagccagccgtggaccctcaaccagcccctgggcatctctgtgttgccgctaaagagccgtttgtaccagaagatgctgacagggaaaggcttggacgggcttcacgtggagcggctccccatcatggacaccagcctgaaaactatcaatgatgaggcccccacagtggctctctccttccagctgctttcctctgagagaccagaaaacttcttgacaccaaacaacagcaaggctcttcctaccttggaccccaagatcctggataagaagctgagaaccatccaagagtcctggtccaaggacacagtgagctccacaatggacttgagcacgtccactccacgagaagcagaggaggaacctctggtgcctgagatgtcccatgacacagagatgaacaactaccggcgggccatgcagaagatggcagaggacatcctgtctctgcggagacaggccagcatcctggaaggagagaaccgcatactgaggagccgcctggcccagcaggaggaggaagaggggcagggcaaagccagtgaggcccagaacacggtgtccatgaagcagaaactgctgctgagtgagctggatatgaagaaactgagggacagggtgcagcatttgcagaatgagctgattcgaaagaatgatcgagagaaggagctgctccttctgtatcaggcccagcagccacaggccgctctgctgaagcagtaccagggcaagctgcagaagatgaaggcgctggaggagactgtgcggcaccaagagaaggtgatcgagaagatggagcgggtgctggaggacaggctgcaggacaggagcaagccccctcctctgaacaggcagcagggaaagccctacacgggcttccctatgctctcagcctctggccttcccttgggttctatgggagagaacctgccggttgaactttactcggtgctgctggcagaaaacgcgaagctgcggacggagctggataagaaccgccaccagcaggcccccatcattctgcagcaacaggccctgccggatctcctctctggtacttcagacaagttcaacctcctggccaagctggaacacgctcagagccggatcctgtccctggaaagccagttagaggactcagctcgacgctggggacgagagaagcaggatctggccacacggctgcaggagcaagaaaaaggtttcaggcacccctcgaactccatcatcatagaacagcctagtgccctcacccactccatggacctcaagcagccctcagagctggagcccctgctgcccagctcagactctaagctcaacaagcccttgagcccccagaaggagaccgctaactctcagcagacctgagccccagagcaggcctccttccctgtgtgctggggagtctcatcaccgccccctaaaaatgacgttattaaatgttgtagctctgtgaaaaaaaaaaaa"
						.toUpperCase());
		this.builderForward.setGeneSymbol("CCDC33");
		this.infoForward = builderForward.build();
		// RefSeq REFSEQ_ID

		GenomeChange change1 = new GenomeChange(
				new GenomePosition(refDict, '+', 15, 74536399, PositionType.ZERO_BASED), "TAAGAAGGAGACCATCA",
				"ACTACCAGAGGAAT");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc002axo.4:exon2:c.96_112delinsACTACCAGAGGAAT:p.Lys33_Met38delinsLeuProGluGluLeu",
				annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.NON_FS_SUBSTITUTION, annotation1.getVariantType());
	}

	@Test
	public void testRealWorldCase_uc011ddm_2_second() throws InvalidGenomeChange {
		this.builderForward = TranscriptModelFactory
				.parseKnownGenesLine(
						refDict,
						"uc011ddm.2	chr5	-	156479371	156485970	156479372	156484954	4	156479371,156482211,156484908,156485931,	156479665,156482544,156485087,156485970,	E9PFX0	uc011ddm.2");
		this.builderForward
				.setSequence("gttacccagcattgtgagtgacagagcctggatctgaacagcaggctcatatgaatcaaccaactgggtgaaaagataagttgcaatctgagatttaagacttgatcagataccatctggtggagggtaccaaccagcctgtctgctcattttccttcaggctgatcccataatgcatcctcaagtggtcatcttaagcctcatcctacatctggcagattctgtagctggttctgtaaaggttggtggagaggcaggtccatctgtcacactaccctgccactacagtggagctgtcacatccatgtgctggaatagaggctcatgttctctattcacatgccaaaatggcattgtctggaccaatggaacccacgtcacctatcggaaggacacacgctataagctattgggggacctttcaagaagggatgtctctttgaccatagaaaatacagctgtgtctgacagtggcgtatattgttgccgtgttgagcaccgtgggtggttcaatgacatgaaaatcaccgtatcattggagattgtgccacccaaggtcacgactactccaattgtcacaactgttccaaccgtcacgactgttcgaacgagcaccactgttccaacgacaacgactgttccaatgacgactgttccaacgacaactgttccaacaacaatgagcattccaacgacaacgactgttctgacgacaatgactgtttcaacgacaacgagcgttccaacgacaacgagcattccaacaacaacaagtgttccagtgacaacaactgtctctacctttgttcctccaatgcctttgcccaggcagaaccatgaaccag"
						.toUpperCase());
		this.builderForward.setGeneSymbol("HAVCR1");
		this.infoForward = builderForward.build();
		// RefSeq NM_012206.2

		GenomeChange change1 = new GenomeChange(
				new GenomePosition(refDict, '+', 5, 156479564, PositionType.ZERO_BASED), "AGTCGT", "GAGCTA");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc011ddm.2:exon4:c.475_480delinsTAGCTC:p.Thr159*", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.STOPGAIN, annotation1.getVariantType());
	}

	@Test
	public void testRealWorldCase_uc001evp_2_second() throws InvalidGenomeChange {
		this.builderForward = TranscriptModelFactory
				.parseKnownGenesLine(
						refDict,
						"uc001evp.2	chr1	-	150768683	150780917	150769274	150779281	8	150768683,150771643,150772019,150776496,150778336,150778577,150779161,150780689,	150769374,150771749,150772185,150776715,150778492,150778700,150779282,150780917,	P43235	uc001evp.2");
		this.builderForward
				.setSequence("acacatgctgcatacacacagaaacactgcaaatccactgcctccttccctcctccctacccttccttctctcagcatttctatccccgcctcctcctcttacccaaattttccagccgatcactggagctgacttccgcaatcccgatggaataaatctagcacccctgatggtgtgcccacactttgctgccgaaacgaagccagacaacagatttccatcagcaggatgtgggggctcaaggttctgctgctacctgtggtgagctttgctctgtaccctgaggagatactggacacccactgggagctatggaagaagacccacaggaagcaatataacaacaaggtggatgaaatctctcggcgtttaatttgggaaaaaaacctgaagtatatttccatccataaccttgaggcttctcttggtgtccatacatatgaactggctatgaaccacctgggggacatgaccagtgaagaggtggttcagaagatgactggactcaaagtacccctgtctcattcccgcagtaatgacaccctttatatcccagaatgggaaggtagagccccagactctgtcgactatcgaaagaaaggatatgttactcctgtcaaaaatcagggtcagtgtggttcctgttgggcttttagctctgtgggtgccctggagggccaactcaagaagaaaactggcaaactcttaaatctgagtccccagaacctagtggattgtgtgtctgagaatgatggctgtggagggggctacatgaccaatgccttccaatatgtgcagaagaaccggggtattgactctgaagatgcctacccatatgtgggacaggaagagagttgtatgtacaacccaacaggcaaggcagctaaatgcagagggtacagagagatccccgaggggaatgagaaagccctgaagagggcagtggcccgagtgggacctgtctctgtggccattgatgcaagcctgacctccttccagttttacagcaaaggtgtgtattatgatgaaagctgcaatagcgataatctgaaccatgcggttttggcagtgggatatggaatccagaagggaaacaagcactggataattaaaaacagctggggagaaaactggggaaacaaaggatatatcctcatggctcgaaataagaacaacgcctgtggcattgccaacctggccagcttccccaagatgtgactccagccagccaaatccatcctgctcttccatttcttccacgatggtgcagtgtaacgatgcactttggaagggagttggtgtgctatttttgaagcagatgtggtgatactgagattgtctgttcagtttccccatttgtttgtgcttcaaatgatccttcctactttgcttctctccacccatgacctttttcactgtggccatcaggactttccctgacagctgtgtactcttaggctaagagatgtgactacagcctgcccctgactgtgttgtcccagggctgatgctgtacaggtacaggctggagattttcacataggttagattctcattcacgggactagttagctttaagcaccctagaggactagggtaatctgacttctcacttcctaagttcccttctatatcctcaaggtagaaatgtctatgttttctactccaattcataaatctattcataagtctttggtacaagtttacatgataaaaagaaatgtgatttgtcttcccttctttgcacttttgaaataaagtatttatctcctgtctacagtttaataaatagcatctagtacacattcaaaaaaaaaaaaaaaa"
						.toUpperCase());
		this.builderForward.setGeneSymbol("HAVCR1");
		this.infoForward = builderForward.build();
		// RefSeq NM_000396.3

		GenomeChange change1 = new GenomeChange(
				new GenomePosition(refDict, '+', 1, 150771702, PositionType.ZERO_BASED), "TG", "CA");
		Annotation annotation1 = new BlockSubstitutionAnnotationBuilder(infoForward, change1).build();
		Assert.assertEquals("uc001evp.2:exon7:c.830_831delinsTG:p.Ala277Val", annotation1.getVariantAnnotation());
		Assert.assertEquals(VariantType.NON_FS_SUBSTITUTION, annotation1.getVariantType());
	}

}
