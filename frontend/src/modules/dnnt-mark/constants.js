export const DnntLinkingMode = Object.freeze({
    Link: 'link',
    Unlink: 'unlink',
    Synchronize: 'sync',
    Clean: 'clean'
});

export const modes = [
    {
        value: DnntLinkingMode.Link,
        text: "přidat"
    },
    {
        value: DnntLinkingMode.Unlink,
        text: "odebrat"
    },
    {
        value: DnntLinkingMode.Synchronize,
        text: "synchronizovat"
    },
    {
        value: DnntLinkingMode.Clean,
        text: "vyčistit"
    }
];
