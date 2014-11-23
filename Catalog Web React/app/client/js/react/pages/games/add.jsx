goog.provide('app.react.pages.games.Add');

/**
 * @constructor
 */
app.react.pages.games.Add = function () {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <div>
        Add game
        </div>
      );
    }
  }));
};
