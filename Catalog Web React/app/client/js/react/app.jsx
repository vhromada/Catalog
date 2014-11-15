goog.provide('app.react.App');

/**
 * @param {app.Routes} routes
 * @param {app.react.Menu} menu
 * @param {app.react.pages.Home} home
 * @param {app.react.pages.games.List} games
 * @constructor
 */
app.react.App = function (routes, menu, home, games) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <div className="app">
          <menu.component/>
          {this.getActivePage()}
        </div>
      );
    },
    getActivePage: function () {
      switch (routes.active) {
        case routes.home:
          return <home.component/>;
        case routes.games:
          return <games.component/>;
      }
    }
  }));
};
