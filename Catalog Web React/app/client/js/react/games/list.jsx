goog.provide('app.react.games.List');

/**
 @param {app.games.GameStore} store
 @param {app.react.Wikipedia} wikipedia
 @constructor
 */
app.react.games.List = function (store, wikipedia) {
  this.component = React.createFactory(React.createClass({
    componentDidMount: function () {
      return store.findAll();
    },
    render: function () {
      var games = store.foundGames.map(
        function (game) {
          return (
            <tr key={game.id}>
              <td>{game.name}</td>
              <td>{game.mediaCount}</td>
              <td>{game.getAdditionalData()}</td>
              <td>{game.note}</td>
              <td>
                <wikipedia.component country={'en'} url={game.wikiEn} text='English Wikipedia'/>
              </td>
              <td>
                <wikipedia.component country={'cz'} url={game.wikiCz} text='Czech Wikipedia'/>
              </td>
            </tr>
          )
        }
      );

      return (
        <table className="table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Count of media</th>
              <th>Additional data</th>
              <th>Note</th>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>{games}</tbody>
        </table>
      );
    }
  }));
};
