goog.provide('app.react.pages.games.Add');

/**
 * @param {app.react.games.Add} add
 * @constructor
 */
app.react.pages.games.Add = function (add) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <div className="add">
          <add.component/>
        </div>
      );
    }
  }));
};
