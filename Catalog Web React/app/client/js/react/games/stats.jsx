goog.provide('app.react.games.Stats');

/**
 @param {app.games.GameStore} store
 @constructor
 */
app.react.games.Stats = function (store) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <table className="table">
          <thead>
            <tr>
              <th>Count of games</th>
              <th>Count of media</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{store.foundGames.length}</td>
              <td>{store.mediaCount}</td>
            </tr>
          </tbody>
        </table>
      );
    }
  }));
};
