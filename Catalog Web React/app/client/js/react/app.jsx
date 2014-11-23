goog.provide('app.react.App');

/**
 * @param {app.Routes} routes
 * @param {app.react.Menu} menu
 * @param {app.react.pages.Home} homePage
 * @param {app.react.pages.games.List} gamesPage
 * @param {app.react.pages.games.Add} addGamePage
 * @constructor
 */
app.react.App = function (routes, menu, homePage, gamesPage, addGamePage) {
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
          return <homePage.component/>;
        case routes.games:
          return <gamesPage.component/>;
        case routes.addGame:
          return <addGamePage.component/>;
      }
    }
  }));
};
