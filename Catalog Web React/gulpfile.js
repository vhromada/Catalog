var gulp = require('gulp');

var GulpEste = require('gulp-este');
var runSequence = require('run-sequence');
var yargs = require('yargs');

var args = yargs.alias('p', 'production').argv;

var este = new GulpEste(__dirname, args.production, '../../../..');

var paths = {
  stylus: [
    'app/client/css/app.styl'
  ],
  coffee: [
    'bower_components/este-library/este/**/*.coffee'
  ],
  jsx: [
    'app/**/*.jsx'
  ],
  js: [
    'bower_components/closure-library/**/*.js',
    'bower_components/este-library/este/**/*.js',
    'app/**/*.js',
    'tmp/**/*.js',
    '!**/build/**'
  ],
  unittest: [
    'app/**/*_test.js',
    'bower_components/este-library/este/**/*_test.js'
  ],
  compiler: 'bower_components/closure-compiler/compiler.jar',
  externs: [
    'app/client/js/externs.js',
    'bower_components/react-externs/externs.js'
  ],
  thirdParty: {
    development: [
      'bower_components/react/react.js'
    ],
    production: [
      'bower_components/react/react.min.js'
    ]
  }
};

var dirs = {
  googBaseJs: 'bower_components/closure-library/closure/goog',
  watch: ['app', 'bower_components/este-library/este']
};

gulp.task('stylus', function () {
  return este.stylus(paths.stylus);
});

gulp.task('coffee', function () {
  return este.coffee(paths.coffee);
});

gulp.task('jsx', function () {
  return este.jsx(paths.jsx);
});

gulp.task('transpile', function (done) {
  return runSequence('stylus', 'coffee', 'jsx', done);
});

gulp.task('deps', function () {
  return este.deps(paths.js);
});

gulp.task('unittest', function () {
  return este.unitTest(dirs.googBaseJs, paths.unittest);
});

gulp.task('dicontainer', function () {
  return este.diContainer(dirs.googBaseJs, [
    {
      name: 'app.DiContainer',
      resolve: ['App']
    },
    {
      name: 'server.DiContainer',
      resolve: ['server.App']
    }
  ]);
});

gulp.task('concat-deps', function () {
  return este.concatDeps();
});

gulp.task('compile-clientapp', function () {
  return este.compile(paths.js, 'app/client/build', {
    compilerPath: paths.compiler,
    compilerFlags: {
      closure_entry_point: 'app.main',
      define: ['goog.array.ASSUME_NATIVE_FUNCTIONS=true'],
      externs: paths.externs
    }
  });
});

gulp.task('compile-serverapp', function () {
  return este.compile(paths.js, 'app/server/build', {
    compilerPath: paths.compiler,
    compilerFlags: {
      closure_entry_point: 'server.main',
      externs: paths.externs.concat(este.getNodeJsExterns()),
      // To have compiled code readable, so we can trace errors.
      debug: true,
      formatting: 'PRETTY_PRINT'
    }
  });
});

gulp.task('concat-all', function () {
  return este.concatAll({
    'app/client/build/app.js': paths.thirdParty
  });
});

gulp.task('livereload-notify', function () {
  return este.liveReloadNotify();
});

gulp.task('js', function (done) {
  return runSequence.apply(null, [
    este.shouldCreateDeps() ? 'deps' : void 0,
    'unittest',
    'dicontainer',
    'concat-deps',
    args.production ? 'compile-clientapp' : void 0,
    args.production ? 'compile-serverapp' : void 0,
    'concat-all',
    este.shouldNotify() ? 'livereload-notify' : void 0,
    done
  ].filter(function (task) {
      return task;
    }));
});

gulp.task('build', function (done) {
  return runSequence('transpile', 'js', done);
});

gulp.task('env', function () {
  return este.setNodeEnv();
});

gulp.task('livereload-server', function () {
  return este.liveReloadServer();
});

gulp.task('watch', function () {
  return este.watch(dirs.watch, {
    css: 'livereload-notify',
    js: 'js',
    jsx: 'jsx',
    styl: 'stylus'
  }, function (task) {
    return gulp.start(task);
  });
});

gulp.task('server', este.bg('node', ['app/server']));

gulp.task('run', function (done) {
  return runSequence.apply(null, [
    'env',
    !args.production ? 'livereload-server' : void 0,
    'watch',
    'server',
    done
  ].filter(function (task) {
      return task;
    }));
});

gulp.task('default', function (done) {
  return runSequence('build', 'run', done);
});

gulp.task('bump', function (done) {
  return este.bump('./*.json', yargs, done);
});
