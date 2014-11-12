goog.provide('app.react.pages.games.List');

/**
 @param {app.react.games.List} list
 @constructor
 */
app.react.pages.games.List = function (list) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <div className="games">
          <header>Games</header>
          <list.component/>
        </div>
      );
    }
  }));
};
