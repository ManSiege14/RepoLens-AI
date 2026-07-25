-- Remove analysis data from repositories
ALTER TABLE repositories
DROP COLUMN IF EXISTS analyzed_at;

-- Remove old analysis collection tables
DROP TABLE IF EXISTS repository_build_tools;
DROP TABLE IF EXISTS repository_frameworks;
DROP TABLE IF EXISTS repository_infrastructure;
DROP TABLE IF EXISTS repository_languages;

-- Create repository_analysis table
CREATE TABLE repository_analysis (

                                     id UUID PRIMARY KEY,

                                     repository_id UUID NOT NULL UNIQUE,

                                     analyzed_at TIMESTAMPTZ NOT NULL,

                                     CONSTRAINT fk_repository_analysis_repository
                                         FOREIGN KEY (repository_id)
                                             REFERENCES repositories(id)
                                             ON DELETE CASCADE
);

-- Build tools
CREATE TABLE repository_analysis_build_tools (

                                                 analysis_id UUID NOT NULL,

                                                 build_tool VARCHAR(255) NOT NULL,

                                                 PRIMARY KEY (analysis_id, build_tool),

                                                 CONSTRAINT fk_repository_analysis_build_tools
                                                     FOREIGN KEY (analysis_id)
                                                         REFERENCES repository_analysis(id)
                                                         ON DELETE CASCADE
);

-- Frameworks
CREATE TABLE repository_analysis_frameworks (

                                                analysis_id UUID NOT NULL,

                                                framework VARCHAR(255) NOT NULL,

                                                PRIMARY KEY (analysis_id, framework),

                                                CONSTRAINT fk_repository_analysis_frameworks
                                                    FOREIGN KEY (analysis_id)
                                                        REFERENCES repository_analysis(id)
                                                        ON DELETE CASCADE
);

-- Infrastructure
CREATE TABLE repository_analysis_infrastructure (

                                                    analysis_id UUID NOT NULL,

                                                    infrastructure VARCHAR(255) NOT NULL,

                                                    PRIMARY KEY (analysis_id, infrastructure),

                                                    CONSTRAINT fk_repository_analysis_infrastructure
                                                        FOREIGN KEY (analysis_id)
                                                            REFERENCES repository_analysis(id)
                                                            ON DELETE CASCADE
);

-- Languages
CREATE TABLE repository_analysis_languages (

                                               analysis_id UUID NOT NULL,

                                               language VARCHAR(255) NOT NULL,

                                               PRIMARY KEY (analysis_id, language),

                                               CONSTRAINT fk_repository_analysis_languages
                                                   FOREIGN KEY (analysis_id)
                                                       REFERENCES repository_analysis(id)
                                                       ON DELETE CASCADE
);