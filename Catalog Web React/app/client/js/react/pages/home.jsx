goog.provide('app.react.pages.Home');

/**
 @param {app.Routes} routes
 @constructor
 */
app.react.pages.Home = function (routes) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <div>
        Home
          <br/>
          <a className="btn btn-default add" href={routes.game.url()}>Game</a>
        </div>
      );
    }
  }));
};
