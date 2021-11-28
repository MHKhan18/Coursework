
const Pokemon = (props) => {

    const parts = props.id.split("/");
    const id = parseInt(parts[parts.length - 2]);


    return (
        < li >
            <div className="card">
                <div className="card-body">
                    <img src={`https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png`}
                        className="card-img-top imageSize" alt="thumbnail" />
                    <p>{props.name || "N/A"}</p>
                </div>
            </div>
        </li >
    );
};

export default Pokemon;