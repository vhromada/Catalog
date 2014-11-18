goog.provide('app.react.pages.games.List');

/**
 * @param {app.react.games.List} list
 * @param {app.react.games.Stats} stats
 * @param {app.Actions} actions
 * @constructor
 */
app.react.pages.games.List = function (list, stats, actions) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      var onUpdatePositions = function () {
        actions.gameUpdatePositions();
      };

      return (
        <div className="games">
          <header>Games</header>
          <button onClick={onUpdatePositions.bind(this)}>Update positions</button>
          <list.component/>
          <stats.component/>
        </div>
      );
    }
  }));
};
