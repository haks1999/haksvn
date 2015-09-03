1. jar에 Users 그룹도 모든 권한을 사용할 수 있도록 변경 필요

2. jar 파일과 같은 위치에 logging.properties 추가

> 아래는 logging.properties 내용
> svnkit.level=FINEST
> svnkit-network.level=FINEST
> svnkit-wc.level=FINEST
> svnkit-cli.level=FINEST
> handlers = java.util.logging.FileHandler

> java.util.logging.FileHandler.pattern = svnkit.%u.log
> java.util.logging.FileHandler.limit = 0
> java.util.logging.FileHandler.count = 1
> java.util.logging.FileHandler.append = true
> java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter

3. haksvn 기동

> java -jar haksvn-0.2.2-war-exec.jar -httpPort 11111 - Djava.util.logging.config.file=logging.properies

4. tagging restore시 아래와 같은 에러 발생-> svnkit.0.log 파일을 열어 확인한다.

> com.haks.haksvn.common.exception.HaksvnException: org.tmatesoft.svn.core.SVNAuthenticationException: svn: E170001: Authentication required for '<svn://xxx.xxx.xxx.xxx:3690> DevOn CI Repository' com.haks.haksvn.repository.dao.SVNRepositoryDao.copyPathToPath(SVNRepositoryDao.java:568)
> com.haks.haksvn.repository.service.SVNRepositoryService.tagging(SVNRepositoryService.java:219)

> 2013. 10. 25 오후 12:52:22 org.tmatesoft.svn.core.internal.util.DefaultSVNDebugLogger log
> 자세히: NETWORK: checking whether connection is stale.
> 2013. 10. 25 오후 12:52:22 org.tmatesoft.svn.core.internal.util.DefaultSVNDebugLogger log
> 자세히: NETWORK: connection is stale: false
> 2013. 10. 25 오후 12:52:22 org.tmatesoft.svn.core.internal.util.DefaultSVNDebugLogger log
> 아주 자세히: SENT
> ( commit ( 158:<<--DELETE EXISTING TAG FOR TAGGING-->>
> [ID](Tagging.md): tagging-3
> [User](Tagging.md): haksvn(haksvn)
> [Description](Description.md):
> Restore From [TAG\_20131023(tagging-1)] by Haksvn ( ) false ( ( 7:svn:log 158:<<--DELETE EXISTING TAG FOR TAGGING-->>
> [ID](Tagging.md): tagging-3
> [User](Tagging.md): haksvn(haksvn)
> [Description](Description.md):
> Restore From [TAG\_20131023(tagging-1)] by Haksvn )  ) ) )
> 2013. 10. 25 오후 12:52:22 org.tmatesoft.svn.core.internal.util.DefaultSVNDebugLogger log
> 아주 자세히: READ
> ( success ( ( CRAM-MD5 ) 19:DevOn CI Repository ) )
> 2013. 10. 25 오후 12:52:22 org.tmatesoft.svn.core.internal.util.DefaultSVNDebugLogger log
> 아주 자세히: SENT
> ( CRAM-MD5 ( ) )
> 2013. 10. 25 오후 12:52:22 org.tmatesoft.svn.core.internal.util.DefaultSVNDebugLogger log
> 아주 자세히: READ
> ( step ( 37:<459753408.1382673142989647@HQDWEBW1> ) )
> 2013. 10. 25 오후 12:52:22 org.tmatesoft.svn.core.internal.util.DefaultSVNDebugLogger log
> 아주 자세히: SENT
> 39:system ee4c36d4db45150c3f86dcb2f331f618
> 2013. 10. 25 오후 12:52:22 org.tmatesoft.svn.core.internal.util.DefaultSVNDebugLogger log
> 아주 자세히: READ
> ( failure ( 18:Username not found ) )

5. svn 계정 설정변경

> 해당 passwd를 가지는 svn 계정을 생성해줌

> haksvn=뭐시기뭐시기

6. haksvn에도 해당 패스워드로 다시 저장

> -> passwd를 못외우므로 이제 이계정은 로그인을 할 수 없어요,ㅋㅋㅋㅋ

7. 다시 tagging을 해보면 알려주신대로 잘 restore 됩디다!!!