export const Table = (props) => (
    <table>
        <thead>
            <tr>
                {props.header.map(h => <th>{h}</th>)}
            </tr>
        </thead>
        <tbody style={{maxHeight: "60%"}}>
            {props.children}
        </tbody>
    </table>
);
