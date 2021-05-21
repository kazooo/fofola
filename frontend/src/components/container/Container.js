export const Container = (props) => (
    <div style={{
        position: "relative",
        width: "max-content",
        margin: "auto"
    }}>
        {props.children}
    </div>
);
