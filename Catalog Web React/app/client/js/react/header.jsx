goog.provide('app.react.Header');

/**
 @constructor
 */
app.react.Header = function () {
  this.component = React.createFactory(React.createClass({
    render: function () {
      return (
        <header>Catalog</header>
      );
    }
  }));
};
