package de.charite.compbio.jannovar.cmd.annotate_vcf;

import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import de.charite.compbio.jannovar.JannovarOptions;
import de.charite.compbio.jannovar.cmd.HelpRequestedException;
import de.charite.compbio.jannovar.cmd.JannovarAnnotationCommandLineParser;

/**
 * Parser for annotate-vcf command line.
 */
public class AnnotateVCFCommandLineParser extends JannovarAnnotationCommandLineParser {

	@Override
	public JannovarOptions parse(String[] argv) throws ParseException, HelpRequestedException {
		// Parse the command line.
		CommandLine cmd = parser.parse(options, argv);

		// Fill the resulting JannovarOptions.
		JannovarOptions result = new JannovarOptions();
		result.printProgressBars = true;
		result.command = JannovarOptions.Command.ANNOTATE_VCF;

		if (cmd.hasOption("help")) {
			printHelp();
			throw new HelpRequestedException();
		}

		if (cmd.hasOption("verbose"))
			result.verbosity = 2;
		if (cmd.hasOption("very-verbose"))
			result.verbosity = 3;

		result.jannovarFormat = cmd.hasOption("janno");

		if (cmd.hasOption("output-dir"))
			result.outVCFFolder = cmd.getOptionValue("output-dir");

		result.jannovarFormat = cmd.hasOption("jannovar");
		result.showAll = cmd.hasOption("showall");

		result.writeJannovarInfoFields = cmd.hasOption("old-info-fields");
		result.writeVCFAnnotationStandardInfoFields = !cmd.hasOption("no-new-info-field");

		String args[] = cmd.getArgs(); // get remaining arguments
		if (args.length < 3)
			throw new ParseException("must have at least two none-option argument, had: " + (args.length - 1));

		result.dataFile = args[1];

		for (int i = 2; i < args.length; ++i)
			result.vcfFilePaths.add(args[i]);

		return result;
	}

	@Override
	protected void initializeParser() {
		super.initializeParser();

		options.addOption(new Option("J", "jannovar", false, "write result in Jannovar output"));
		options.addOption(new Option("a", "showall", false,
				"report annotations for all affected transcripts (by default only one "
						+ "with the highest impact is shown for each alternative allele)"));
		options.addOption(new Option("o", "output-dir", true,
				"output directory (default is to write parallel to input file)"));

		options.addOption(new Option("", "old-info-fields", false,
				"write out old Jannovar VCF INFO fields \"EFFECT\" and \"HGVS\" (default is off)"));
		options.addOption(new Option("", "no-new-info-field", false,
				"do not write out the new VCF annotation standard INFO field \"ANN\" (default is on)"));
	}

	private void printHelp() {
		final String HEADER = new StringBuilder()
		.append("Jannovar Command: annotate\n\n")
		.append("Use this command to annotate a VCF file.\n\n")
		.append("Usage: java -jar de.charite.compbio.jannovar.jar annotate [options] <database> [<IN.VCF>]+\n\n")
		.toString();
		final String FOOTER = new StringBuilder().append(
				"\n\nExample: java -jar de.charite.compbio.jannovar.jar annotate data/hg19_ucsc.ser IN.vcf\n\n")
				.toString();

		System.err.print(HEADER);

		HelpFormatter hf = new HelpFormatter();
		PrintWriter pw = new PrintWriter(System.err, true);
		hf.printOptions(pw, 78, options, 2, 2);

		System.err.print(FOOTER);
	}
}
