package com.repolens.analysis.clone;

import java.nio.file.Path;

/**
 * Clones a remote Git repository into a temporary directory.
 */
public interface RepositoryCloner {

    /**
     * Clones the given repository into a temporary directory.
     *
     * @param repositoryUrl URL of the Git repository.
     * @return path to the cloned repository.
     * @throws RepositoryCloneException if cloning fails.
     */
    Path cloneRepository(String repositoryUrl);

}