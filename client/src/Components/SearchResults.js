import React, {Component} from "react";
import {Table} from "react-bootstrap";

class SearchResults extends Component {
    render() {
        let searchResults;
        if (this.props.searchResults.length > 0) {
            searchResults = this.props.searchResults.map(renderSearchResults);
        }

        function renderSearchResults(result) {
            return (<tr key={result.id}>
                <td>{result.name}</td>
                <td>{result.carbohydrates}</td>
                <td>{result.fat}</td>
                <td>{result.protein}</td>
                <td>{result.calories}</td>
            </tr>);
        }

        return <div>
            <Table striped bordered condensed hover>
                <thead>
                <tr>
                    <th>Food Name</th>
                    <th>Carbohydrates</th>
                    <th>Fat</th>
                    <th>Proteins</th>
                    <th>Calories</th>
                </tr>
                </thead>
                <tbody>
                {searchResults}
                </tbody>
            </Table>
        </div>;
    }
}


export default SearchResults;