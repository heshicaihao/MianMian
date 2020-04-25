package com.itheima.mm.service;

import com.itheima.mm.constants.Constants;
import com.itheima.mm.dao.QuestionDao;
import com.itheima.mm.dao.QuestionItemDao;
import com.itheima.mm.dao.TagDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.pojo.QuestionItem;
import com.itheima.mm.pojo.Tag;
import com.itheima.mm.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuyp
 * @date 2020/03/06
 */
public class QuestionService {
    public PageResult pageList(QueryPageBean queryPageBean) throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        QuestionDao dao = session.getMapper(QuestionDao.class);

        //根据搜索条件，查询数量
        Long total = dao.total(queryPageBean);
        //根据搜索条件，查询题目列表
        List<Question> questionList = dao.pageList(queryPageBean);
        SqlSessionFactoryUtils.commitAndClose(session);

        return new PageResult(total, questionList);
    }

    public void save(Question question) throws IOException {
        SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        QuestionDao questionDao = session.getMapper(QuestionDao.class);

        //判断是新增题目，还是修改题目
        Integer id = question.getId();
        if (id == 0) {
            //新增题目
            //1. 保存题目信息
            //1.1 补全状态数据。发布状态：待发布0；审核状态：待审核0
            question.setStatus(Constants.QUESTION_PRE_PUBLISH);
            question.setReviewStatus(Constants.QUESTION_PRE_REVIEW);
            //1.2 保存题目数据，要得到最新的主键值
            questionDao.add(question);

            //2. 保存题目选项信息。只有单选和多选题目，才有选项数据需要保存
            List<QuestionItem> questionItemList = question.getQuestionItemList();
            if (questionItemList != null && questionItemList.size()>0) {
                QuestionItemDao itemDao = session.getMapper(QuestionItemDao.class);

                //客户端提交的有题目选项信息，需要挨个保存到数据库t_question_item表
                for (QuestionItem questionItem : questionItemList) {
                    //如果选项里有内容，说明是有效的选项；否则是无效选项（用户没有录入这一项数据），不需要保存
                    String content = questionItem.getContent();
                    if (content != null && !"".equals(content)) {
                        //需要有：选项所属的题目id
                        questionItem.setQuestionId(question.getId());
                        itemDao.add(questionItem);
                    }
                }
            }

            //3. 保存题目 和 标签的关系
            List<Tag> tagList = question.getTagList();
            if (tagList != null && tagList.size() > 0) {
                TagDao tagDao = session.getMapper(TagDao.class);

                Map<String, Object> map = new HashMap<>();
                map.put("questionId", question.getId());

                for (Tag tag : tagList) {
                    //保存的是标签数据？还是  题目和标签的关系？。保存的是关系。tr_question_tag
                    //在新增题目的时候，选择了 题目和哪些标签有关系，并非要创建标签对象

                    //要执行的SQL是：insert into tr_question_tag (question_id, tag_id) values (?, ?)
                    //假如：questionId是1， tagId是1
                    map.put("tagId", tag.getId());
                    tagDao.addTagAndQuestionRef(map);
                }
            }
        }else{
            //TODO 修改题目
        }

        SqlSessionFactoryUtils.commitAndClose(session);
    }
}
