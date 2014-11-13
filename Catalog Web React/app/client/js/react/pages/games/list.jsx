goog.provide('app.react.pages.games.List');

/**
 @param {app.react.games.List} list
 @param {app.react.games.Stats} stats
 @constructor
 */
app.react.pages.games.List = function (list, stats) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <div className="games">
          <header>Games</header>
          <list.component/>
          <stats.component/>
        </div>
      );
    }
  }));
};
