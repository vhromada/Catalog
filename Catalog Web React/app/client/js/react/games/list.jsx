goog.provide('app.react.games.List');

/**
 @param {app.games.Store} store
 @constructor
 */
app.react.games.List = function (store) {
  this.component = React.createFactory(React.createClass({
    componentDidMount: function () {
      store.findAll();
    },
    render: function () {
      var games = store.foundGames.map(
        function (game) {
          return (
            <tr key={game.id}>
              <td>{game.id}</td>
              <td>{game.name}</td>
              <td>{game.wikiEn}</td>
              <td>{game.wikiCz}</td>
              <td>{game.mediaCount}</td>
              <td>{game.crack ? 'true' : 'false'}</td>
              <td>{game.serialKey ? 'true' : 'false'}</td>
              <td>{game.patch ? 'true' : 'false'}</td>
              <td>{game.trainer ? 'true' : 'false'}</td>
              <td>{game.trainerData ? 'true' : 'false'}</td>
              <td>{game.editor ? 'true' : 'false'}</td>
              <td>{game.saves ? 'true' : 'false'}</td>
              <td>{game.otherData}</td>
              <td>{game.note}</td>
              <td>{game.position}</td>
            </tr>
          )
        }
      );

      return (
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>English Wikipedia</th>
              <th>Czech Wikipedia</th>
              <th>Count of media</th>
              <th>Crack</th>
              <th>Serial key</th>
              <th>Patch</th>
              <th>Trainer</th>
              <th>Data for trainer</th>
              <th>Editor</th>
              <th>Saves</th>
              <th>Other data</th>
              <th>Note</th>
              <th>Position</th>
            </tr>
          </thead>
          <tbody>{games}</tbody>
        </table>
      );
    }
  }));
};
