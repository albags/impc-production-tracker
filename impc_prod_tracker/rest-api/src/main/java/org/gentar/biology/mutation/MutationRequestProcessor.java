package org.gentar.biology.mutation;

import org.gentar.biology.location.Location;
import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.sequence.Sequence;
import org.gentar.biology.sequence_location.SequenceLocation;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;

@Component
public class MutationRequestProcessor
{
    private MutationUpdateMapper mutationUpdateMapper;

    public MutationRequestProcessor(MutationUpdateMapper mutationUpdateMapper)
    {
        this.mutationUpdateMapper = mutationUpdateMapper;
    }

    /**
     * Updates an mutation with the information than can be updated in a dto.
     * @param originalMutation The original mutation.
     * @param mutationUpdateDTO the dto with the new information.
     * @return the updated mutation.
     */
    public Mutation getMutationToUpdate(Mutation originalMutation, MutationUpdateDTO mutationUpdateDTO)
    {
        if (originalMutation == null || mutationUpdateDTO == null)
        {
            throw new UserOperationFailedException("Cannot update the mutation");
        }
        Mutation newMutation = new Mutation(originalMutation);

       Mutation mappedMutation = mutationUpdateMapper.toEntity(mutationUpdateDTO);
        newMutation.setMgiAlleleSymbolRequiresConstruction(
            mappedMutation.getMgiAlleleSymbolRequiresConstruction());
        newMutation.setGeneticMutationType(mappedMutation.getGeneticMutationType());
        newMutation.setMolecularMutationType(mappedMutation.getMolecularMutationType());
        if (mutationUpdateDTO.getSymbolOrAccessionIds() != null)
        {
            newMutation.setGenes(mappedMutation.getGenes());
        }
        newMutation.setMutationCategorizations(mappedMutation.getMutationCategorizations());
        setMutationQcResults(newMutation, mappedMutation);
        setMutationSequences(originalMutation, newMutation, mappedMutation);
        return newMutation;
    }

    private void setMutationQcResults(Mutation newMutation, Mutation mappedMutation)
    {
        Set<MutationQcResult> mappedMutationQcResults = mappedMutation.getMutationQcResults();
        newMutation.setMutationQcResults(mappedMutationQcResults);
        if (mappedMutationQcResults != null)
        {
            mappedMutationQcResults.forEach(x -> x.setMutation(newMutation));
        }
    }

    private void setMutationSequences(Mutation originalMutation, Mutation newMutation, Mutation mappedMutation)
    {
        Set<MutationSequence> mappedMutationSequences = mappedMutation.getMutationSequences();

        if (mappedMutationSequences != null)
        {
            // This is needed because the association with the mutation does not change, so this
            // allows to keep the reference that can be lost in the mapping.
            for (MutationSequence mutationSequence : mappedMutationSequences)
            {
                mutationSequence.setMutation(originalMutation);
                if (mutationSequence.getId() != null)
                {
                    MutationSequence originalMutationSequence =
                        getMutationSequenceById(
                            mutationSequence.getId(), originalMutation.getMutationSequences());
                    if (originalMutationSequence != null)
                    {
                        Sequence originalSequence = originalMutationSequence.getSequence();
                        Sequence newSequence = mutationSequence.getSequence();
                        completeSequence(originalSequence, newSequence);
                    }
                }
            }
            newMutation.setMutationSequences(mappedMutationSequences);
        }
    }

    private void completeSequence(Sequence originalSequence, Sequence newSequence)
    {
        if (originalSequence != null && newSequence != null)
        {
            List<SequenceLocation> originalSequenceLocations = originalSequence.getSequenceLocations();
            List<SequenceLocation> newSequenceLocations = newSequence.getSequenceLocations();
            completeSequenceLocations(originalSequenceLocations, newSequenceLocations);
        }
    }

    /// Set the Sequence location if they are not new and need one
    private void completeSequenceLocations(
        List<SequenceLocation> originalSequenceLocations, List<SequenceLocation> newSequenceLocations)
    {
        for (SequenceLocation newSequenceLocation : newSequenceLocations)
        {
            Long id = newSequenceLocation.getId();
            if (id != null)
            {
                // The entity exists so it should exist in the original object
                SequenceLocation originalSequenceLocation = originalSequenceLocations.stream()
                    .filter(x -> x.getId().equals(id)).findAny().orElse(null);
                completeLocation(originalSequenceLocation, newSequenceLocation);
            }
        }
    }

    // Set missing data to the new location
    private void completeLocation(SequenceLocation originalEntity, SequenceLocation newEntity)
    {
        Location originalLocation = originalEntity.getLocation();
        Location newLocation = newEntity.getLocation();
        if (originalLocation != null && newLocation != null)
        {
            newLocation.setSequenceLocations(originalLocation.getSequenceLocations());
        }
    }

    private MutationSequence getMutationSequenceById(Long id, Set<MutationSequence> mutationSequences)
    {
        MutationSequence original = null;
        if (mutationSequences != null)
        {
            original = mutationSequences.stream()
                .filter(x -> x.getId().equals(id))
                .findAny().orElse(null);
        }
        return original;
    }
}