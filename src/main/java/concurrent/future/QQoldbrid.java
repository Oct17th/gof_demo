package concurrent.future;

public class QQoldbrid {
	/**
	 * 查询朋友圈帖子的接口
	 * */
	@Override
	public List<TopicResultVO> findRosterTopicList(String username,int pageNum)throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<TopicResultVO> result = new ArrayList<TopicResultVO>();
		
		try {
			//得到要使用的mapper对象
			TopicMapper topicMapper = sqlSession.getMapper(TopicMapper.class);
			ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);
			UserTopicMapMapper userTopicMapMapper = sqlSession.getMapper(UserTopicMapMapper.class);
			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
			PictureMapper pictureMapper = sqlSession.getMapper(PictureMapper.class);
//			LawroleMapper lawroleMapper = sqlSession.getMapper(LawroleMapper.class);
			CommentMapper commentMapper = sqlSession.getMapper(CommentMapper.class);
			ClicklikeMapper clicklikeMapper = sqlSession.getMapper(ClicklikeMapper.class);
			CollectionsMapper collectionsMapper = sqlSession.getMapper(CollectionsMapper.class);
			MbinfoMapper mbMapper = sqlSession.getMapper(MbinfoMapper.class);
			FollowersMapper followersMapper = sqlSession.getMapper(FollowersMapper.class);
			
			//根据用户名查找可见的帖子
			UserTopicMap paramUserTopicMap = new UserTopicMap();
			paramUserTopicMap.setUsername(username);
			paramUserTopicMap.setPermission(Constants.TOPIC_ISVISIBLE);
			List<UserTopicMap> userTopicMapList = userTopicMapMapper.selectUserTopicMapByUsernameAndPermission(paramUserTopicMap);
			
			//配置参数
			List<String> topicNolist = new ArrayList<String>();
			for(UserTopicMap tmpuserTopicMap : userTopicMapList){
				topicNolist.add(tmpuserTopicMap.getTopicNo());
			}
			
			//当有可看的帖子的时候
			if(topicNolist.size()>0){
				HashMap<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("topicNolist", topicNolist);
				//得到topic列表
				PageInfo<Topic> pageinfo = selectRosterTopicListForPages(topicMapper,paramMap, pageNum, Constants.REFRESH_NEARUSER_COUNT);
				List<Topic> topiclist = pageinfo.getList();
				
//				System.out.println("朋友圈--第一页取出的条数----------------"+topiclist.size());
				int totalpages = pageinfo.getPages();
				
				//取出topicVO
				for(Topic tmptopic : topiclist){
					//得到帖子图片
					List<String> imagepath = new ArrayList<String>();
					Image paramImage = new Image();
					paramImage.setTopicNo(tmptopic.getTopicNo());
					List<Image> images = imageMapper.selectImageByTopicNo(paramImage);
					for(Image tmpImage : images){
						imagepath.add(tmpImage.getImagepath());
					}
					TopicVO topicVO = null;
					
					/******************当是转载的帖子的时候---begin******************/
					String soureTopicNo = tmptopic.getSourceTopicNo();
					if(!StringUtils.isBlank(soureTopicNo)){
						Topic paramTopic = new Topic();
						paramTopic.setTopicNo(soureTopicNo);
						Topic oldtopic = topicMapper.selectTopicByTopicNo(paramTopic);
						
						String oldcontent = null;
						try {
							oldcontent = oldtopic.getContent();//原帖子的content
						} catch (Exception e) {
							logger.info("原帖子内容：content为null！！！！");
//							e.printStackTrace();
						}
						
						//得到原帖子的用户头像
						String olduserheadimg = null;//原帖子的用户头像
						Picture oldPicture = new Picture();
						oldPicture.setUsername(oldtopic.getUsername());
						List<Picture> oldpicturelist = pictureMapper.selectPictureByUsername(oldPicture);
						
						for(Picture picture : oldpicturelist){
							String tmpimagetype = picture.getImagetype();
							
							if(Constants.HEAD_IMAGE.equals(tmpimagetype)){
								olduserheadimg = picture.getImagepath();
							}
						}
						
						
						Map<String,Object> oldtopicMap = new HashMap<String, Object>();
						oldtopicMap.put("oldcontent", oldcontent);
						oldtopicMap.put("olduserheadimg", olduserheadimg);
						topicVO.setOldtopic(oldtopicMap);
					}
					/******************当是转载的帖子的时候---end********************/
					
					
					//得到帖子的用户
					User paramuser = new User();
					paramuser.setUsername(tmptopic.getUsername());
					User user = userMapper.selectUserByUsername(paramuser);
					//如果帖子没有用户就跳出
					if(user==null){
						continue;
					}else{
						user.setPassword(null);	
					}
					
					//得到用户的相关图片
					Picture paramPicture = new Picture();
					paramPicture.setUsername(tmptopic.getUsername());
					List<Picture> picturelist = pictureMapper.selectPictureByUsername(paramPicture);
					
					List<Picture> imgforheadlist = new ArrayList<Picture>();//用户图像图片
					List<Picture> imgforlovelist = new ArrayList<Picture>();//喜爱的风景图片
					for(Picture picture : picturelist){
						String tmpimagetype = picture.getImagetype();
						
						if(Constants.HEAD_IMAGE.equals(tmpimagetype)){
							imgforheadlist.add(picture);
						}else if(Constants.LOVE_IMAGE.equals(tmpimagetype)){
							imgforlovelist.add(picture);
						}
					}
					
					//添加好友备注
					String friendRealname = user.getUsername();
					String remark = XMPPUserUtils.getInstance().getUserRosterFriendNick(username, friendRealname);
					
					//得到用户的相关角色????????
					//List<Lawrole> lawrolelist = lawroleMapper.selectLawroleByTypeAndIndustry(lawrole)
					UserVO userVO = new UserVO(user,imgforheadlist,imgforlovelist,null);
					
					//获取viplevel和level,以后从缓存里取
					boolean retSetUserLevels = setUserLevels(mbMapper, userVO, tmptopic.getUsername());
					
					//是否点赞
					Clicklike paramClicklike = new Clicklike();
					paramClicklike.setTopicNo(tmptopic.getTopicNo());
					paramClicklike.setUsername(username);
					int isClickOrCannel;
					Clicklike hasClicklike = clicklikeMapper.selectClicklikeByTopicNoAndUsername(paramClicklike);
					if(hasClicklike!=null){
						isClickOrCannel = hasClicklike.getIsclicklike();
					}else{
						isClickOrCannel = 0;
					}
					
					//添加点赞人数
					List<Map<String,String>> clicklikeuserandheadimgs = new ArrayList<Map<String,String>>();
					List<Clicklike> clicklikelist = clicklikeMapper.selectClicklikeByTopicNo(paramClicklike);
					if(clicklikelist!=null&&clicklikelist.size()>0){
						List<String> rosters = new ArrayList<String>();
						List<String> rosterlist = XMPPUserUtils.getInstance().getUserRoster(username);
						for(String rostername :rosterlist){
							rosters.add(XMPPUserUtils.getInstance().getBareUserName(rostername));
						}
						
						for(Clicklike val:clicklikelist){
							//过滤掉取消点赞的情况
						    if(val.getIsclicklike()<0){continue;}
							String uname = val.getUsername();
							//if(rosters.contains(uname)){//2016-11-17为了解决点赞看不到问题而注释的
								Map<String,String> valmap = getUsernameAndHeadImgMapByUsername(pictureMapper, userMapper,val.getUsername());
								if(valmap!=null&&!valmap.isEmpty()){
									clicklikeuserandheadimgs.add(valmap);
								}
							//}
						}
					}
					tmptopic.setClickcout(clicklikeuserandheadimgs.size());
					topicVO = new TopicVO(tmptopic, imagepath);
					//是否收藏
					Collections collections = new Collections();
					collections.setTopicNo(tmptopic.getTopicNo());
					collections.setUsername(username);
					int isCollections;
					Collections hasCollections = collectionsMapper.selectCollectionsByTopicNoAndUsername(collections);
					if(hasCollections!=null){
						isCollections = 1;
					}else{
						isCollections = 0;
					}
					
					
					//添加关注字段
					int ismutual = -1;
					if (StringUtils.isNotEmpty(username)) {
						FollowersExample example = new FollowersExample();
						example.createCriteria().andBloggerEqualTo(tmptopic.getUsername()).andFollowerEqualTo(username);
						List<Followers> followers = followersMapper.selectByExample(example);
						if(followers.size()!=0){
							ismutual = followers.get(0).getIsmutual();
						}
					}
					
					//取出帖子的评论
					Comment parametercomment = new Comment();
					parametercomment.setTopicNo(tmptopic.getTopicNo());
					List<Comment> commentlist = commentMapper.selectCommentByTopicNo(parametercomment);
					List<CommentVO> commentVOlist = new ArrayList<CommentVO>();
					
					commentVOlist = getCommentVO(userMapper, pictureMapper, username, commentlist);
					/*//添加图像
					for(Comment comment : commentlist){
						Map<String,Object> commentuser = new HashMap<String, Object>();
						//添加用户头像信息
						String imagepathvalue = null;
						Picture picture = new Picture();
						picture.setUsername(comment.getChildusername());
						List<Picture> resultPicture = pictureMapper.selectPictureByUsername(picture);
						for(Picture pic :resultPicture){
							if(pic!=null&&Constants.HEAD_IMAGE.equals(pic.getImagetype())&&!StringUtils.isBlank(pic.getImagepath())){
								imagepathvalue = pic.getImagepath();
								break;
							}
						}
						commentuser.put("imagepath", imagepathvalue);
						
						//添加用户名
						String name = comment.getChildusername();//用户名
						commentuser.put("username", name);
						
						//添加昵称
						String nickname = null;
						User paramu = new User();
						paramu.setUsername(name);
						User hasuser = userMapper.selectUserByUsername(paramu);
						if(hasuser!=null){
							nickname = hasuser.getNickname();
						}
						commentuser.put("nickname", nickname);//昵称
						
						//添加备注
						String commentremark = XMPPUserUtils.getInstance().getUserRosterFriendNick(username, name);
						commentuser.put("remark", commentremark);
						
						commentVOlist.add(new CommentVO(comment,commentuser));
					}*/
					TopicResultVO vo = new TopicResultVO(userVO, topicVO, commentVOlist, isClickOrCannel, remark,totalpages,isCollections,clicklikeuserandheadimgs);
					vo.setIsmutual(ismutual);
					
					//添加帖子的可见性用户列表
					List<Map<String,Object>> visibleusernames = new ArrayList<Map<String, Object>>();
					
					Map<String,Object> pp = new HashMap<String, Object>();
					pp.put("topicNo", tmptopic.getTopicNo());
					pp.put("permission", Constants.TOPIC_ISVISIBLE);
					List<UserTopicMap> lst = userTopicMapMapper.selectUserTopicMapByTopicNoAndPermission(pp);
					for(UserTopicMap mapt : lst){
						if(mapt!=null&&!StringUtils.isBlank(mapt.getUsername())){
							Map<String,Object> tm = new HashMap<String, Object>();
							tm.put("username", mapt.getUsername());
							
							User paramu = new User();
							paramu.setUsername(mapt.getUsername());
							User tu = userMapper.selectUserByUsername(paramu);
							
							if(tu!=null){
								tm.put("nickname", tu.getNickname());
							}
							
							
							Picture paramp = new Picture();
							paramp.setUsername(mapt.getUsername());
							paramp.setImagetype("图像图片");
							List<Picture> plst = pictureMapper.selectPictureByUsernameAndImagetype(paramp);
							
							String imagepathvalue = null;
							if(plst!=null&&plst.size()>0){
								imagepathvalue = plst.get(0).getImagepath();
							}
							tm.put("imagepath", imagepathvalue);
							visibleusernames.add(tm);
						}
					}
					vo.setVisibleusernames(visibleusernames);
					result.add(vo);
				}
			}
			
			sqlSession.commit();
		}finally{
			sqlSession.close();
		}
		
		return result;
	}
	
}
