export const sections = [
    {
        name: 'home.section.displayMetadata',
        services: [
            {
                name: 'feature.uuidInfo.title',
                route: '/uuid-info',
                enabled: true,
            },
            {
                name: 'feature.solrQuery.title',
                route: '/solr-query',
                enabled: true,
            },
            {
                name: 'feature.krameriusProcess.title',
                route: '/kramerius-processes',
                enabled: true,
            },
            {
                name: 'feature.pdf.title',
                route: '/pdf',
                enabled: false,
            },
        ],
    },
    {
        name: 'home.section.editMetadata',
        services: [
            {
                name: 'feature.access.title',
                route: '/access',
                enabled: true,
            },
            {
                name: 'feature.reindex.title',
                route: '/reindex',
                enabled: true,
            },
            {
                name: 'feature.delete.title',
                route: '/delete',
                enabled: true,
            },
            {
                name: 'feature.perioPartsPublish.title',
                route: '/perio-parts-publish',
                enabled: true,
            },
        ],
    },
    {
        name: 'home.section.virtualCollection',
        services: [
            {
                name: 'feature.vcManagement.title',
                route: '/vc',
                enabled: true,
            },
            {
                name: 'feature.linkVc.title',
                route: '/link-vc',
                enabled: true,
            },
            {
                name: 'feature.linkDonator.title',
                route: '/link-donator',
                enabled: false,
            },
            {
                name: 'feature.checkDonator.title',
                route: '/check-donator',
                enabled: true,
            },
        ],
    },
    {
        name: 'home.section.sugo',
        services: [
            // {
            //     name: 'feature.dnntInfo.title',
            //     route: '/dnnt-info',
            // },
            {
                name: 'feature.dnntMark.title',
                route: '/dnnt-mark',
                enabled: false,
            },
            {
                name: 'feature.dnntTransitions.title',
                route: '/dnnt-transition',
                enabled: true,
            },
            {
                name: 'feature.dnntSessions.title',
                route: '/dnnt-session',
                enabled: true,
            },
            {
                name: 'feature.dnntJobs.title',
                route: '/dnnt-job',
                enabled: false,
            },
            {
                name: 'feature.dnntAlerts.title',
                route: '/dnnt-alert',
                enabled: true,
            },
        ],
    },
    {
        name: 'home.section.internal',
        services: [
            {
                name: 'feature.internalProcesses.title',
                route: '/internal-processes',
                enabled: true,
            },
        ],
    },
];
