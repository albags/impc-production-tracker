/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.biology.project;

import org.gentar.audit.history.History;
import org.gentar.biology.project.search.filter.ProjectFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProjectService
{
    Project getProjectByTpn(String tpn);
    Project getNotNullProjectByTpn(String tpn);
    List<Project> getProjects(ProjectFilter projectFilter);
    Page<Project> getProjects(ProjectFilter projectFilter, Pageable pageable);

    Project createProject(Project project);

    /**
     * Updates a project.
     * @param newProject Project with the new data.
     * @return A record of {@link History} with the fields that changed in the object.
     */
    History updateProject(Project oldProject, Project newProject);

    /**
     * Gets the history for a project
     * @param project The project.
     * @return List of {@link History} with the trace of the changes for a project.
     */
    List<History> getProjectHistory(Project project);

    /**
     * Checks if the project needs to be updated. This is to be called but entities whose changes
     * affect a project (changes in a plan, eg.)
     * @param project The project to check
     */
    void checkForUpdates(Project project);

    void updateConflictingProjects(Project project);
}
