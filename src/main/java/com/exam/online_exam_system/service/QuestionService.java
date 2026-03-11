package com.exam.online_exam_system.service;

import com.exam.online_exam_system.exception.ResourceNotFoundException;
import com.exam.online_exam_system.model.Exam;
import com.exam.online_exam_system.model.Question;
import com.exam.online_exam_system.repository.QuestionRepository;
import com.exam.online_exam_system.repository.StudentAnswerRepository;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class QuestionService {

  @Autowired private QuestionRepository questionRepository;

  @Autowired private StudentAnswerRepository studentAnswerRepository;

  public Question addQuestion(Question question) {
    if (question == null) {
      throw new IllegalArgumentException("Question cannot be null");
    }
    return questionRepository.save(question);
  }

  public Question updateQuestion(Long id, Question updatedQuestion) {
    Question existing = getQuestionById(id);
    existing.setQuestionText(updatedQuestion.getQuestionText());
    existing.setOptionA(updatedQuestion.getOptionA());
    existing.setOptionB(updatedQuestion.getOptionB());
    existing.setOptionC(updatedQuestion.getOptionC());
    existing.setOptionD(updatedQuestion.getOptionD());
    existing.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
    existing.setDifficulty(updatedQuestion.getDifficulty());
    existing.setMarks(updatedQuestion.getMarks());
    existing.setExam(updatedQuestion.getExam());
    return questionRepository.save(existing);
  }

  public List<Question> getAllQuestions() {
    return questionRepository.findAll();
  }

  public Question getQuestionById(Long id) {

    if (id == null) {
      throw new IllegalArgumentException("Id cannot be null");
    }

    return questionRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Question", id));
  }

  @Transactional
  @SuppressWarnings("null")
  public void deleteQuestion(Long id) {
    Question question =
        questionRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

    studentAnswerRepository.deleteByQuestion(question);
    questionRepository.delete(question);
  }

  @Transactional
  public void importQuestionsFromExcel(MultipartFile file, Exam exam) throws Exception {
    List<Question> questions = new ArrayList<>();
    try (InputStream is = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(is)) {

      Sheet sheet = workbook.getSheetAt(0);
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) continue;

        Cell textCell = row.getCell(0);
        if (textCell == null || textCell.getCellType() == CellType.BLANK) continue;

        Question q = new Question();
        q.setExam(exam);
        q.setQuestionText(getCellValueAsString(textCell));
        q.setOptionA(getCellValueAsString(row.getCell(1)));
        q.setOptionB(getCellValueAsString(row.getCell(2)));
        q.setOptionC(getCellValueAsString(row.getCell(3)));
        q.setOptionD(getCellValueAsString(row.getCell(4)));

        String corr = getCellValueAsString(row.getCell(5));
        if (corr != null) {
          corr = corr.trim();
          // Handle numeric correct answers (e.g. "1" for A, "2" for B)
          if (corr.equals("1")) corr = "A";
          else if (corr.equals("2")) corr = "B";
          else if (corr.equals("3")) corr = "C";
          else if (corr.equals("4")) corr = "D";

          if (corr.length() == 1 && corr.toUpperCase().matches("[ABCD]")) {
            q.setCorrectAnswer(corr.toUpperCase());
          } else {
            if (q.getOptionA() != null && corr.equalsIgnoreCase(q.getOptionA().trim()))
              q.setCorrectAnswer("A");
            else if (q.getOptionB() != null && corr.equalsIgnoreCase(q.getOptionB().trim()))
              q.setCorrectAnswer("B");
            else if (q.getOptionC() != null && corr.equalsIgnoreCase(q.getOptionC().trim()))
              q.setCorrectAnswer("C");
            else if (q.getOptionD() != null && corr.equalsIgnoreCase(q.getOptionD().trim()))
              q.setCorrectAnswer("D");
            else q.setCorrectAnswer(corr.toUpperCase());
          }
        }

        // Set Difficulty and Marks from column 6 (7th column) if exists, else default to Easy
        Cell diffCell = row.getCell(6);
        String diff = (diffCell != null) ? getCellValueAsString(diffCell) : "Easy";
        if (diff == null || diff.isEmpty()) diff = "Easy";

        q.setDifficulty(diff);
        int marks = 1;
        if ("Medium".equalsIgnoreCase(diff)) marks = 3;
        else if ("Hard".equalsIgnoreCase(diff)) marks = 5;
        q.setMarks(marks);

        questions.add(q);
      }
    }
    questionRepository.saveAll(questions);
  }

  private String getCellValueAsString(Cell cell) {
    if (cell == null) return null;
    if (cell.getCellType() == CellType.STRING) {
      return cell.getStringCellValue();
    } else if (cell.getCellType() == CellType.NUMERIC) {
      double val = cell.getNumericCellValue();
      if (val == Math.floor(val)) {
        return String.valueOf((long) val);
      }
      return String.valueOf(val);
    }
    try {
      return cell.toString();
    } catch (Exception e) {
      return null;
    }
  }

  public long count() {
    return questionRepository.count();
  }

  public List<Question> getQuestionsByExam(Long examId) {
    return questionRepository.findByExamId(examId);
  }

  public List<Question> searchQuestions(String query) {
    if (query == null || query.trim().isEmpty()) {
      return getAllQuestions();
    }
    return questionRepository.findByQuestionTextContainingIgnoreCase(query);
  }

  public List<Question> searchQuestionsByExam(Long examId, String query) {
    if (examId == null || examId == -1) {
      return searchQuestions(query);
    }
    if (query == null || query.trim().isEmpty()) {
      return getQuestionsByExam(examId);
    }
    return questionRepository.findByExamIdAndQuestionTextContainingIgnoreCase(examId, query);
  }
}
