package de.charite.compbio.jannovar.annotation;

import java.util.ArrayList;

import com.google.common.collect.ImmutableMap;

import de.charite.compbio.jannovar.annotation.builders.AnnotationBuilderDispatcher;
import de.charite.compbio.jannovar.annotation.builders.StructuralVariantAnnotationBuilder;
import de.charite.compbio.jannovar.impl.intervals.IntervalArray;
import de.charite.compbio.jannovar.io.Chromosome;
import de.charite.compbio.jannovar.io.ReferenceDictionary;
import de.charite.compbio.jannovar.reference.GenomeChange;
import de.charite.compbio.jannovar.reference.GenomeInterval;
import de.charite.compbio.jannovar.reference.GenomePosition;
import de.charite.compbio.jannovar.reference.PositionType;
import de.charite.compbio.jannovar.reference.TranscriptModel;

// TODO(holtgrem): We should directly pass in a JannovarData object after adding the interval trees to it. Then, this should be fine.

/**
 * Main driver class for annotating variants.
 *
 * Given, a chromosome map, objects of this class can be used to annotate variants identified by a genomic position
 * (chr, pos), a reference, and an alternative nucleotide String.
 *
 * @author Manuel Holtgrewe <manuel.holtgrewe@charite.de>
 * @author Marten Jaeger <marten.jaeger@charite.de>
 * @author Peter N Robinson <peter.robinson@charite.de>
 */
public final class VariantAnnotator {

	/** {@link ReferenceDictionary} to use for genome information. */
	final private ReferenceDictionary refDict;

	/** {@link Chromosome}s with their {@link TranscriptInfo} objects. */
	final private ImmutableMap<Integer, Chromosome> chromosomeMap;

	/**
	 * This object will be used to prioritize the annotations and to choose the one(s) to report. For instance, if we
	 * have both an intronic and a nonsense mutation, just report the nonsense mutation. Note that the object will be
	 * initialized once in the constructor of the Chromosome class and will be reset for each new annotation, rather
	 * than creating a new object for each variation. Also note that the constructor takes an integer value with which
	 * the lists of potential annotations get initialized. We will take 2*SPAN because this is the maximum number of
	 * annotations any variant can get with this program.
	 */
	final private AnnotationCollector annovarFactory = new AnnotationCollector(20);

	/**
	 * Construct new VariantAnnotator, given a chromosome map.
	 *
	 * @param refDict
	 *            {@link ReferenceDictionary} with information about the genome.
	 * @param chromosomeMap
	 *            chromosome map to use for the annotator.
	 */
	public VariantAnnotator(ReferenceDictionary refDict, ImmutableMap<Integer, Chromosome> chromosomeMap) {
		this.refDict = refDict;
		this.chromosomeMap = chromosomeMap;
	}

	// TODO(holtgrem): Remove this?
	/**
	 * Convenience function for obtaining an {@link AnnotationList} from genome change in primitive types.
	 *
	 * Forwards to {@link #buildAnnotationList(int, int, String, String, PositionType)} and we recommend to use this
	 * function directly.
	 *
	 * @param position
	 *            The start position of the variant on this chromosome (one-based numbering)
	 * @param ref
	 *            String representation of the reference sequence affected by the variant
	 * @param alt
	 *            String representation of the variant (alt) sequence
	 * @param posType
	 *            the position type to use
	 * @return {@link AnnotationList} for the given genome change
	 * @throws AnnotationException
	 *             on problems building the annotation list
	 */
	public AnnotationList buildAnnotationList(int chr, int position, String ref, String alt, PositionType posType)
			throws AnnotationException {
		// Get chromosome by id.
		if (chromosomeMap.get(chr) == null)
			throw new AnnotationException(String.format("Could not identify chromosome \"%d\"", chr));

		// Build the GenomeChange to build annotation for.
		GenomePosition pos = new GenomePosition(refDict, '+', chr, position, posType);
		GenomeChange change = new GenomeChange(pos, ref, alt);

		return buildAnnotationList(change);
	}

	/**
	 * Main entry point to getting Annovar-type annotations for a variant identified by chromosomal coordinates.
	 *
	 * When we get to this point, the client code has identified the right chromosome, and we are provided the
	 * coordinates on that chromosome.
	 *
	 * @param change
	 *            the {@link GenomeChange} to annotate
	 * @return {@link AnnotationList} for the genome change
	 * @throws AnnotationException
	 *             on problems building the annotation list
	 */
	public AnnotationList buildAnnotationList(GenomeChange change) throws AnnotationException {
		change = change.withPositionType(PositionType.ZERO_BASED);
		final GenomeInterval changeInterval = change.getGenomeInterval();

		/* The following command "resets" the annovarFactory object */
		this.annovarFactory.clearAnnotationLists();

		// Get the TranscriptModel objects that overlap with changeInterval.
		final Chromosome chr = chromosomeMap.get(change.getChr());
		IntervalArray<TranscriptModel>.QueryResult qr;
		if (changeInterval.length() == 0)
			qr = chr.getTMIntervalTree().findOverlappingWithPoint(changeInterval.beginPos);
		else
			qr = chr.getTMIntervalTree().findOverlappingWithInterval(changeInterval.beginPos, changeInterval.endPos);
		ArrayList<TranscriptModel> candidateTranscripts = new ArrayList<TranscriptModel>(qr.entries);

		// Handle the case of no overlapping transcript. Then, create intergenic, upstream, or downstream annotations
		// and return the result.
		boolean isStructuralVariant = (change.ref.length() >= 1000 || change.alt.length() >= 1000);
		if (candidateTranscripts.isEmpty()) {
			if (isStructuralVariant)
				buildSVAnnotation(change, null);
			else
				buildNonSVAnnotation(change, qr.left, qr.right);
			return annovarFactory.getAnnotationList();
		}

		// If we reach here, then there is at least one transcript that overlaps with the query. Iterate over these
		// transcripts and collect annotations for each (they are collected in annovarFactory).
		for (TranscriptModel tm : candidateTranscripts)
			if (isStructuralVariant)
				buildSVAnnotation(change, tm);
			else
				buildNonSVAnnotation(change, tm);

		return annovarFactory.getAnnotationList();
	}

	private void buildSVAnnotation(GenomeChange change, TranscriptModel transcript) throws AnnotationException {
		annovarFactory.addStructuralAnnotation(new StructuralVariantAnnotationBuilder(transcript, change).build());
	}

	private void buildNonSVAnnotation(GenomeChange change, TranscriptModel leftNeighbor, TranscriptModel rightNeighbor)
			throws AnnotationException {
		buildNonSVAnnotation(change, leftNeighbor);
		buildNonSVAnnotation(change, rightNeighbor);
	}

	private void buildNonSVAnnotation(GenomeChange change, TranscriptModel transcript) throws InvalidGenomeChange {
		if (transcript != null) // TODO(holtgrew): Is not necessarily an exonic annotation!
			annovarFactory.addExonicAnnotation(new AnnotationBuilderDispatcher(transcript, change).build());
	}

}
