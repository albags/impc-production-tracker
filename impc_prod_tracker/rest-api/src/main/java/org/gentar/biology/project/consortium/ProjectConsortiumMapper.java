package org.gentar.biology.project.consortium;

import org.gentar.organization.consortium.ConsortiumService;
import org.gentar.biology.project.ProjectConsortiumDTO;
import org.gentar.biology.project.ProjectConsortiumInstituteDTO;
import org.gentar.EntityMapper;
import org.gentar.organization.institute.Institute;
import org.springframework.stereotype.Component;
import org.gentar.biology.project.consortium.institute.ProjectConsortiumInstituteMapper;

import java.util.*;

@Component
public class ProjectConsortiumMapper {
    private EntityMapper entityMapper;
    private ConsortiumService consortiumService;
    private ProjectConsortiumInstituteMapper projectConsortiumInstituteMapper;

    public ProjectConsortiumMapper(ConsortiumService consortiumService, EntityMapper entityMapper, ProjectConsortiumInstituteMapper projectConsortiumInstituteMapper)
    {
        this.entityMapper = entityMapper;
        this.consortiumService = consortiumService;
        this.projectConsortiumInstituteMapper = projectConsortiumInstituteMapper;
    }

    public ProjectConsortium toEntity(ProjectConsortiumDTO projectConsortiumDTO)
    {
        ProjectConsortium projectConsortium = new ProjectConsortium();
        String consortiumName = projectConsortiumDTO.getConsortiumName();
        projectConsortium.setConsortium(consortiumService.getConsortiumByNameOrThrowException(consortiumName));
        projectConsortium.setInstitutes(addProjectConsortiumInstituteFromEntity(projectConsortiumDTO));

        return projectConsortium;
    }

    public Set<ProjectConsortium> toEntities(Collection<ProjectConsortiumDTO> projectConsortiumDTOS)
    {
        Set<ProjectConsortium> projectConsortia = new HashSet<>();
        if (projectConsortiumDTOS != null)
        {
            projectConsortiumDTOS.forEach(x -> projectConsortia.add(toEntity(x)));
        }
        return projectConsortia;
    }

    public ProjectConsortiumDTO toDto(ProjectConsortium projectConsortium)
    {
        ProjectConsortiumDTO projectConsortiumDTO = entityMapper.toTarget(projectConsortium, ProjectConsortiumDTO.class);
        addProjectConsortiumInstituteFromDto(projectConsortium, projectConsortiumDTO);
        return projectConsortiumDTO;
    }

    public List<ProjectConsortiumDTO> toDtos(Collection<ProjectConsortium> projectConsortium)
    {
        List<ProjectConsortiumDTO> consortiumDTOS = new ArrayList<>();
        if (projectConsortium != null)
        {
            projectConsortium.forEach(x -> consortiumDTOS.add(toDto(x)));
        }
        return consortiumDTOS;
    }

    private void addProjectConsortiumInstituteFromDto(ProjectConsortium projectConsortium, ProjectConsortiumDTO projectConsortiumDTO)
    {
        List<ProjectConsortiumInstituteDTO> projectConsortiumInstituteDTOS =
                projectConsortiumInstituteMapper.toDtos(projectConsortium.getInstitutes());
        projectConsortiumDTO.setProjectConsortiumInstituteDTOS(projectConsortiumInstituteDTOS);
    }

    private Set<Institute> addProjectConsortiumInstituteFromEntity(ProjectConsortiumDTO projectConsortiumDTO)
    {
        Set<Institute> institutes = projectConsortiumInstituteMapper.toEntities(projectConsortiumDTO.getProjectConsortiumInstituteDTOS());

        return institutes;
    }

}
