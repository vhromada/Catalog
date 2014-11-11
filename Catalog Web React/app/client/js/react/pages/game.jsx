goog.provide('app.react.pages.Game');

/**
 @param {app.Routes} routes
 @constructor
 */
app.react.pages.Game = function (routes) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <div>
        Game
          <br/>
          <a className="btn btn-default add" href={routes.home.url()}>Home</a>
        </div>
      );
    }
  }));
};
