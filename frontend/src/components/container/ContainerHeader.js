export const ContainerHeader = (props) => (
    <div style={{
        background: "rgba(255, 255, 255, 0.5)",
        borderRadius: "5px",
        border: "1px solid black",
        width: "fit-content",
        height: "fit-content",
    }}>
        {props.children}
    </div>
);
