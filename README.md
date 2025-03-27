# Guidelines
- All changes should be developed on a separate branch and have pr opened while being developed.
- All features should have a corresponding issue and be tracked in the github project.
- All pull requests should be code reviewed by a senior member or mentor before being merged in.
## Writing a subsystem
1) Think about what you need your hardware to do(write the IO layer).
2) Write your logic using that io interface(write the subsystem file).
3) Write your sim io layer, then test your logic using that.
4) Learn how to tune using your sim layer.
5) Then write your real io layer.
6) Tune in real life.
7) Clean up code and start a code review.
