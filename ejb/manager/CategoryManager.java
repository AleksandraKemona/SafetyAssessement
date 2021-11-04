/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.lodz.p.it.spjava.e11.sa.ejb.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import pl.lodz.p.it.spjava.e11.sa.dto.AnalysisDTO;
import pl.lodz.p.it.spjava.e11.sa.dto.CategoryDTO;
import pl.lodz.p.it.spjava.e11.sa.ejb.facade.AnalysisFacade;
import pl.lodz.p.it.spjava.e11.sa.ejb.facade.CategoryFacade;
import pl.lodz.p.it.spjava.e11.sa.entity.Analysis;
import pl.lodz.p.it.spjava.e11.sa.entity.Category;
import pl.lodz.p.it.spjava.e11.sa.exception.AppBaseException;
import pl.lodz.p.it.spjava.e11.sa.utils.converter.AnalysisConverter;


@Stateful
public class CategoryManager extends AbstractManager {
    @EJB
    private CategoryFacade categoryFacade;
    
    @EJB
    private AnalysisFacade analysisFacade;
    
    
    
    private Category appliedCategory;
    
    public List<Analysis> createCategory(Category newCategory, List<AnalysisDTO> selectedAnalysisList) throws AppBaseException{
         List<Analysis> demandedAnalysisEntity = AnalysisConverter.createListAnalysisEntityFromDTO(selectedAnalysisList);

        newCategory.setDemandsAnalysis(demandedAnalysisEntity);

        
        categoryFacade.create(newCategory);
        return demandedAnalysisEntity;
    }
    
    public List<Category> downloadAllCategories() throws AppBaseException{
        return categoryFacade.findAll();
    }
    
    public Category downloadCategoryForCosmetic(Long id) {
        appliedCategory = categoryFacade.find(id);
//        if (null == edytowaneKonto)
//            throw ...;
        categoryFacade.refresh(appliedCategory);
        return appliedCategory;
    }


    public void saveCategoryAfterEdition(Category tmp, List<AnalysisDTO> selectedAnalysisList) throws AppBaseException{
        List<Analysis> oldAnalysis = tmp.getDemandsAnalysis();
        List<Analysis> demandedAnalysisEntity = AnalysisConverter.createListAnalysisEntityFromDTO(selectedAnalysisList);
        List<Analysis> demandedAnalysisEntity2 = new ArrayList<>();
        for (Analysis analysis : demandedAnalysisEntity) {
            if (oldAnalysis.contains(analysis)) {
                analysis=null;  
                demandedAnalysisEntity2.add(analysis);
            }else{
                demandedAnalysisEntity2.add(analysis);
            }   
        }
        System.out.println("list demanded Analysis 2 "+ demandedAnalysisEntity2);
        tmp.setDemandsAnalysis(demandedAnalysisEntity2);
        categoryFacade.edit(tmp);
    }
    
    public Category downloadCategoryForDetails(Long id) {
        Category downloadedCategory = categoryFacade.find(id);
//        if (null == pobieraneKonto)
//            throw ...;
        categoryFacade.refresh(downloadedCategory);
        return downloadedCategory;
    }
    
    public void chooseAnalysis(final CategoryDTO categoryDTO) {
        List<Analysis> demandedAnalysis = AnalysisConverter.createListAnalysisEntityFromDTO(categoryDTO.getDemandsAnalysis());
        List<Analysis> chosenAnalysisList = new ArrayList<>();
        for (Analysis analysis : demandedAnalysis) {
            Analysis chosenAnalysis = analysisFacade.findAnalysisByName(analysis.getName());
            chosenAnalysisList.add(chosenAnalysis);
        }
        Category category = new Category();
        category.setDemandsAnalysis(chosenAnalysisList);
     }
    
}
