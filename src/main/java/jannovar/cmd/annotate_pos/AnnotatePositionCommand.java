package jannovar.cmd.annotate_pos;

import jannovar.JannovarOptions;
import jannovar.annotation.AnnotationList;
import jannovar.annotation.VariantAnnotator;
import jannovar.cmd.JannovarAnnotationCommand;
import jannovar.common.ChromosomeMap;
import jannovar.exception.AnnotationException;
import jannovar.exception.JannovarException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows the annotation of a single position.
 *
 * @author Manuel Holtgrewe <manuel.holtgrewe@charite.de>
 */
public class AnnotatePositionCommand extends JannovarAnnotationCommand {

	public AnnotatePositionCommand(JannovarOptions options) {
		super(options);
	}

	/**
	 * THis function will simply annotate given chromosomal position with HGVS compliant output e.g. chr1:909238G>C -->
	 * PLEKHN1:NM_032129.2:c.1460G>C,p.(Arg487Pro)
	 *
	 * @throws AnnotationException
	 */
	@Override
	public void run() throws JannovarException {
		System.err.println("input: " + options.chromosomalChange);
		Pattern pat = Pattern.compile("(chr[0-9MXY]+):([0-9]+)([ACGTN])>([ACGTN])");
		Matcher mat = pat.matcher(options.chromosomalChange);

		if (!mat.matches() | mat.groupCount() != 4) {
			System.err
					.println("[ERROR] Input string for the chromosomal change does not fit the regular expression ... :(");
			System.exit(3);
		}

		byte chr = ChromosomeMap.identifier2chromosom.get(mat.group(1));
		int pos = Integer.parseInt(mat.group(2));
		String ref = mat.group(3);
		String alt = mat.group(4);

		VariantAnnotator annotator = new VariantAnnotator(chromosomeMap);
		AnnotationList anno = annotator.getAnnotationList(chr, pos, ref, alt);
		if (anno == null) {
			String e = String.format("No annotations found for variant %s", options.chromosomalChange);
			throw new AnnotationException(e);
		}
		String annotation;
		String effect;
		if (options.showAll) {
			annotation = anno.getAllTranscriptAnnotations();
			effect = anno.getAllTranscriptVariantEffects();
		} else {
			annotation = anno.getSingleTranscriptAnnotation();
			effect = anno.getVariantType().toString();
		}

		System.out.println(String.format("EFFECT=%s;HGVS=%s", effect, annotation));
	}
}
