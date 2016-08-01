if exist "%USERPROFILE%\.ivy2\local\com.dslplatform.traitorlist" rmdir /S /Q "%USERPROFILE%\.ivy2\local\com.dslplatform.traitorlist"

cd "%~dp0"
call sbt clean core/publishLocal

if exist foobar rmdir /S /Q foobar
call scalac ^
  -P:traitor-list:output:example.txt ^
  -P:traitor-list:traitors:net.revenj.patterns.DomainEventHandler ^
  "-Xplugin:%USERPROFILE%\.ivy2\local\com.dslplatform.traitorlist\traitor-list_2.11\0.0.1\jars\traitor-list_2.11.jar" ^
  -cp "%USERPROFILE%\.ivy2\local\net.revenj\revenj-core_2.11\0.1.1-SNAPSHOT\jars\revenj-core_2.11.jar" ^
  example\src\main\scala\foobar\*.scala
