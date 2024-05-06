# Output

The output is a set of files with actions and errors (when encountered).  The files maybe `txt`, `sql` or `markdown`.  You may want to use a `markdown` viewer for easier viewing of those reports.  The markdown viewer needs to support [github markdown tables](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#tables) .

The default output (when `-o` is NOT used), will be `$HOME/.hive-sre/hive-sre-output/<sub-command>/<timestamp>`.

When `-o` is used, the output will be in the specified directory and will NOT include the `<sub-command>` directory or timestamp.  This is a behavior change from previous versions (pre 3.0.1.4).

If you wish to use a different output directory AND desire the `<sub-command>/<timestamp>` directory structure, create a symlink to the desired output directory as `$HOME/.hive-sre/hive-sre-output`.

