export const FileUploadWithButton = ({label, submitFunc}) => {

    const handleFileChosen = e => {
        const fileUploaded = e.target.files[0];
        submitFunc(fileUploaded);
    }

    return <form className="form-inline">
        <label>{label}</label>
        <input
            type='file'
            accept='.jpg'
            onChange={handleFileChosen}
        />
    </form>;
};
