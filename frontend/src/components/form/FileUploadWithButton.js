export const FileUploadWithButton = ({label, submitFunc, acceptTypes}) => {

    const handleFileChosen = e => {
        const fileUploaded = e.target.files[0];
        submitFunc(fileUploaded);
    }

    return <form className="form-inline">
        <label>{label}</label>
        <input
            type='file'
            accept={acceptTypes}
            onChange={handleFileChosen}
        />
    </form>;
};
