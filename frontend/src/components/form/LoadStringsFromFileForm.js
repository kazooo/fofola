import {IconButton} from 'components/button';

export const LoadStringsFromFileForm = ({label, submitFunc}) => {
    let fileReader;

    const handleFileRead = () => {
        const uuids = [];
        const lines = fileReader.result.split(/[\r\n]+/);
        for (let i = 0; i < lines.length; i++) {
            const line = lines[i];
            line && line !== '' && uuids.push(line)
        }
        submitFunc(uuids);
    };

    const handleFileChosen = (e) => {
        e.preventDefault();
        const file = e.target.files[0];
        e.target.value = null;
        fileReader = new FileReader();
        fileReader.onloadend = handleFileRead;
        fileReader.readAsText(file);
    };

    return <IconButton>
        {label}
        <input
            type='file'
            hidden
            accept='.txt'
            onChange={handleFileChosen}
        />
    </IconButton>;
};
