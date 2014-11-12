goog.provide('app.react.Menu');

/**
 @param {app.Routes} routes
 @constructor
 */
app.react.Menu = function (routes) {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <nav className="navbar navbar-default">
          <div className="navbar-header">
            <a className="navbar-brand" href={routes.home.url()}>Catalog</a>
          </div>
          <ul className="nav navbar-nav">
            <li>
              <a href={routes.games.url()}>Games</a>
            </li>
          </ul>
        </nav>
      );
    }
  }));
};
