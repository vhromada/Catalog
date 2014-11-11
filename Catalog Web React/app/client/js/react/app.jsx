goog.provide('app.react.App');

/**
 @param {app.Routes} routes
 @param {app.react.Header} header
 @param {app.react.pages.Home} homePage
 @param {app.react.pages.Game} gamePage
 @constructor
 */
app.react.App = function (routes, header, homePage, gamePage) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <div className="app">
          <header.component/>
          {this.getActivePage()}
        </div>
      );
    },
    getActivePage: function () {
      switch (routes.active) {
        case routes.home:
          return <homePage.component/>;
        case routes.game:
          return <gamePage.component/>;
      }
    }
  }));
};
