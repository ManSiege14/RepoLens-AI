ALTER TABLE repositories
ADD COLUMN analyzed_at TIMESTAMPTZ;
CREATE TABLE repository_build_tools (
                                        repository_id UUID NOT NULL,
                                        build_tool VARCHAR(255) NOT NULL,

                                        PRIMARY KEY (repository_id, build_tool),

                                        CONSTRAINT fk_repository_build_tools
                                            FOREIGN KEY (repository_id)
                                                REFERENCES repositories(id)
                                                ON DELETE CASCADE
);

CREATE TABLE repository_frameworks (
                                       repository_id UUID NOT NULL,
                                       framework VARCHAR(255) NOT NULL,

                                       PRIMARY KEY (repository_id, framework),

                                       CONSTRAINT fk_repository_frameworks
                                           FOREIGN KEY (repository_id)
                                               REFERENCES repositories(id)
                                               ON DELETE CASCADE
);

CREATE TABLE repository_infrastructure (
                                           repository_id UUID NOT NULL,
                                           infrastructure VARCHAR(255) NOT NULL,

                                           PRIMARY KEY (repository_id, infrastructure),

                                           CONSTRAINT fk_repository_infrastructure
                                               FOREIGN KEY (repository_id)
                                                   REFERENCES repositories(id)
                                                   ON DELETE CASCADE
);

CREATE TABLE repository_languages (
                                      repository_id UUID NOT NULL,
                                      language VARCHAR(255) NOT NULL,

                                      PRIMARY KEY (repository_id, language),

                                      CONSTRAINT fk_repository_languages
                                          FOREIGN KEY (repository_id)
                                              REFERENCES repositories(id)
                                              ON DELETE CASCADE
);