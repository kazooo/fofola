import './style.css';

export const TextFileReadWithButton = ({label, submitFunc}) => {

    let fileReader;

    const handleFileRead = () => {
        const uuids = [];
        const lines = fileReader.result.split('\n');
        for (let line = 0; line < lines.length; line++) {
            uuids.push(lines[line])
        }
        submitFunc(uuids);
    };

    const handleFileChosen = (file) => {
        fileReader = new FileReader();
        fileReader.onloadend = handleFileRead;
        fileReader.readAsText(file);
    };

    return <form className="form-inline">
        <label>{label}</label>
        <input
            type='file'
            accept='.txt'
            onChange={e => handleFileChosen(e.target.files[0])}
        />
    </form>;
}
