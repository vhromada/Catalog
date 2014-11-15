goog.provide('app.react.games.List');

/**
 * @param {app.games.Store} store
 * @param {app.react.Wikipedia} wikipedia
 * @param {app.react.Move} move
 * @param {app.Actions} actions
 * @constructor
 */
app.react.games.List = function (store, wikipedia, move, actions) {
  this.component = React.createFactory(React.createClass({
    componentDidMount: function () {
      return store.findAll();
    },
    render: function () {
      var onDuplicateClick = function (id) {
        actions.gameDuplicate(id);
      };

      var onMoveUp = function (id) {
        actions.gameMoveUp(id);
      };

      var onMoveDown = function (id) {
        actions.gameMoveDown(id);
      };

      var onRemove = function (id) {
        actions.gameRemove(id);
      };

      var games = store.games.map(
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
              <td>
                <button onClick={onDuplicateClick.bind(this, game.id)}>Duplicate</button>
              </td>
              <td>
                <move.component up={true} list={store.games} item={game} text='Move up' action={onMoveUp}/>
              </td>
              <td>
                <move.component up={false} list={store.games} item={game} text='Move down' action={onMoveDown}/>
              </td>
              <td>
                <button onClick={onRemove.bind(this, game.id)}>Remove</button>
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
              <th></th>
              <th></th>
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
